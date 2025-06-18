package com.earth.ureverse.inspector.service;

import com.earth.ureverse.global.common.exception.AiInspectionException;
import com.earth.ureverse.global.mapper.ProductMapper;
import com.earth.ureverse.global.notification.dto.NotificationDto;
import com.earth.ureverse.global.notification.service.NotificationService;
import com.earth.ureverse.global.util.S3Service;
import com.earth.ureverse.inspector.dto.query.AiInspectorQueryDto;
import com.earth.ureverse.inspector.mapper.InspectorMapper;
import com.earth.ureverse.member.dto.query.NotificationQueryDto;
import com.earth.ureverse.member.mapper.NotificationMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.InputStream;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
@PropertySource("classpath:application.properties")
public class AiServiceImpl implements AiService {

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.api.url}")
    private String apiUrl;

    private final InspectorMapper inspectorMapper;
    private final NotificationMapper notificationMapper;
    private final ProductMapper productMapper;
    private final S3Service s3Service;
    private final NotificationService notificationService;
    private static final int MAX_RETRY = 10;

    @Override
    public void aiInspect(List<String> imgUrls, String category, String brandName, Long productId, String senderName, Long userId) {
        ObjectMapper mapper = new ObjectMapper();
        List<Map<String, Object>> imageParts = new ArrayList<>();

        try {
            // 이미지 S3 다운로드
            for (String accessUrl : imgUrls) {
                String key = extractKeyFromUrl(accessUrl);
                String presignedGetUrl = s3Service.generatePresignedGetUrl(key);

                try (InputStream in = new URL(presignedGetUrl).openStream()) {
                    byte[] imageBytes = in.readAllBytes();
                    imageParts.add(Map.of(
                            "inlineData", Map.of(
                                    "mimeType", "image/jpeg",
                                    "data", Base64.getEncoder().encodeToString(imageBytes)
                            )
                    ));
                }
            }

            // 프롬프트 + 요청 바디 구성
            String prompt = buildPrompt(category);
            Map<String, Object> textPart = Map.of("text", prompt);
            List<Map<String, Object>> parts = new ArrayList<>(imageParts);
            parts.add(textPart);
            Map<String, Object> requestBody = Map.of("contents", List.of(Map.of("parts", parts)));

            // 최대 10회 재시도
            int attempt = 0;
            while (attempt < MAX_RETRY) {
                attempt++;
                RestTemplate restTemplate = new RestTemplate();
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                HttpEntity<String> requestEntity = new HttpEntity<>(mapper.writeValueAsString(requestBody), headers);

                String fullUrl = apiUrl + "?key=" + apiKey;
                ResponseEntity<String> response = restTemplate.exchange(fullUrl, HttpMethod.POST, requestEntity, String.class);

                // 응답 파싱
                JsonNode root = mapper.readTree(response.getBody());
                String responseText = root.path("candidates").get(0)
                        .path("content").path("parts").get(0).path("text").asText();
                String cleanedJson = responseText.replaceAll("(?i)```json", "").replaceAll("```", "").trim();
                JsonNode resultJson = mapper.readTree(cleanedJson);

                // 필수값 누락 시 재시도
                if (isNullOrEmpty(resultJson, "result") ||
                        isNullOrEmpty(resultJson, "isTorn") ||
                        isNullOrEmpty(resultJson, "hasStain") ||
                        isNullOrEmpty(resultJson, "hasFading") ||
                        isNullOrEmpty(resultJson, "isStretched") ||
                        isNullOrEmpty(resultJson, "otherDefect") ||
                        isNullOrEmpty(resultJson, "notes")) {
                    log.info("AI 응답 누락 → 재시도 {}", attempt);
                    continue;
                }

                // 정상 응답일 경우 inspector DB 저장
                AiInspectorQueryDto dto = AiInspectorQueryDto.builder()
                        .productId(productId)
                        .inspectMethod("AI")
                        .notes(resultJson.path("notes").asText())
                        .result(resultJson.path("result").asText().toUpperCase())
                        .isTorn(resultJson.path("isTorn").asText().toUpperCase())
                        .hasStain(resultJson.path("hasStain").asText().toUpperCase())
                        .hasFading(resultJson.path("hasFading").asText().toUpperCase())
                        .isStretched(resultJson.path("isStretched").asText().toUpperCase())
                        .otherDefect(resultJson.path("otherDefect").asText().toUpperCase())
                        .senderName(senderName)
                        .build();
                inspectorMapper.insert(dto, productId);

                // 결과 메시지 분기 처리
                String result = dto.getResult(); // "PASS" or "FAIL"
                String message;
                // 상품 테이블 status 값
                String status;

                if ("PASS".equalsIgnoreCase(result)) {
                    message = String.format(
                            "등록하신 [%s] 브랜드의 [%s] 상품이 AI 검수를 통과했습니다. 곧 최종 검수가 진행될 예정입니다.",
                            brandName, category
                    );
                    status = "FIRST_INSPECT";
                } else {
                    message = String.format(
                            "등록하신 [%s] 브랜드의 [%s] 상품이 AI 검수에 실패했습니다. 다시 한 번 확인 후 재등록 부탁드립니다.",
                            brandName, category
                    );
                    status = "REJECT";
                }

                // 저장 현재시작
                LocalDateTime now = LocalDateTime.now();

                // notification pk 저장
                Long notificationId = notificationMapper.getNextNotificationId();

                String notificationType = "AI_RESULT";
                String title = "AI 검수결과 안내";


                // notification DB 저장
                NotificationQueryDto notificationQueryDto = NotificationQueryDto.builder()
                                .notificationId(notificationId)
                                .notificationType(notificationType)
                                .userId(userId)
                                .title(title)
                                .message(message)
                                .isRead("N")
                                .timestamp(now)
                                .createUserId(1L)
                                .build();
                notificationMapper.insert(notificationQueryDto);

                // product 테이블 status 갱신
                productMapper.updateProductAfterInspection(productId, null, null, status, 1L);

                // SSE 알림 추가
                notificationService.sendNotification(new NotificationDto(
                        notificationId,
                        userId,
                        title,
                        message,
                        notificationType,
                        "N",
                        now
                ));
                return; // 성공 후 종료
            }

            // 최대 재시도 초과 시 예외 발생 → 아래 catch로 이동
            throw new AiInspectionException("AI 응답 누락으로 최대 재시도 초과");

        } catch (Exception e) {
            // 실패 시 관련 데이터 삭제
            try {
                productMapper.deleteById(productId);
            } catch (Exception ignored) {
            }
            log.error("AI 검사 실패로 product {} 관련 데이터 삭제됨", productId);
            throw new AiInspectionException("AI 검사 실패 및 데이터 삭제 완료", e);
        }
    }

    // key 추출 메서드 추가
    private String extractKeyFromUrl(String accessUrl) {
        // "https://bucket.s3.region.amazonaws.com/uploads/abc.jpg" → "uploads/abc.jpg"
        return accessUrl.substring(accessUrl.indexOf("uploads"));
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    private String buildPrompt(String category) {
        return  "아래 이미지는 의류의 앞/뒷면이며, 이 의류는 \"" + category + "\" 카테고리에 속한다.\n" +
                "이 카테고리를 기준으로 손상 여부를 판단하고, 결과를 JSON 형식으로 반환한다.\n\n" +
                "\n" +
                "- result (검수결과): pass / fail 중 하나를 선택\n" +
                "- 각 손상 항목(isTorn, hasStain, hasFading, isStretched, otherDefect)은 Y 또는 N으로 판단\n" +
                "- notes 항목에는 Y로 표시된 손상에 대한 설명을 간결하게 기술\n" +
                "\n" +
                "[result 분류 기준]\n" +
                "- pass: 모든 세부 항목이 손상 없음 (모두 N), 손상은 있으나 사람이 판단해야 할 수준 (예: 조명, 주름, 디자인과 혼동), 확실한 손상이지만 경미한 경우\n" +
                "- fail: 심각한 손상이 존재하거나, 사진이 흐리거나 어둡거나 가려져 판단 불가능한 경우, 사진이 카테고리와 명백히 불일치하는 경우, 각 사진이 확실히 서로 다른 의류일 경우, 제품의 앞, 뒷면 사진 중 하나라도 없는 경우\n" +
                "\n" +
                "[손상 항목 정의 - Y/N]\n" +
                "- isTorn: 찢어짐, 스크래치\n" +
                "- hasStain: 얼룩, 오염\n" +
                "- hasFading: 색 바램\n" +
                "- isStretched: 옷의 늘어남\n" +
                "- otherDefect: 이미지가 카테고리와 불일치하거나, 두 이미지가 같은 의류가 아니거나, 품질 문제로 판단이 어려운 경우\n" +
                "\n" +
                "[추가 지침]\n" +
                "- 손상 여부가 애매하거나 디자인 요소와 구분이 어려운 경우에도 Y로 표시하고, notes에 그 사유를 작성한다.\n" +
                "\n" +
                "[결과 JSON 구조 예시]\n" +
                "{\n" +
                "  \"result\": \"pass | fail\",\n" +
                "  \"isTorn\": \"Y | N\",\n" +
                "  \"hasStain\": \"Y | N\",\n" +
                "  \"hasFading\": \"Y | N\",\n" +
                "  \"isStretched\": \"Y | N\",\n" +
                "  \"otherDefect\": \"Y | N\",\n" +
                "  \"notes\": \"...\"\n" +
                "}\n" +
                "\n응답 결과는 코드 블록 없이 순수 JSON 형식으로만 출력해 주세요. notes 는 한글로 작성해 주세요.\n";
    }

    private boolean isNullOrEmpty(JsonNode node, String fieldName) {
        return !node.has(fieldName) || node.path(fieldName).asText().isBlank();
    }
}
