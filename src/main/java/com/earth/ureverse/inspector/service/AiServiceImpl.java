package com.earth.ureverse.inspector.service;

import com.earth.ureverse.global.util.S3Service;
import com.earth.ureverse.inspector.dto.query.AiInspectorQueryDto;
import com.earth.ureverse.inspector.mapper.InspectorMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@PropertySource("classpath:application.properties")
public class AiServiceImpl implements AiService {

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.api.url}")
    private String apiUrl;

    private final InspectorMapper inspectorMapper;
    private final S3Service s3Service;

    @Override
    public void aiInspect(List<String> imgUrls, String category, Long productId, String senderName) throws IOException {
        // JSON 요청 생성
        ObjectMapper mapper = new ObjectMapper();

        List<Map<String, Object>> imageParts = new ArrayList<>();
        for (String accessUrl : imgUrls) {
            try {
                // accessUrl에서 key 추출
                String key = extractKeyFromUrl(accessUrl); // 아래 메서드 정의 참고

                // presigned GET URL 생성
                String presignedGetUrl = s3Service.generatePresignedGetUrl(key);

                // AI 서버 통신 위한 헤더, 객체 생성
                URL url = new URL(presignedGetUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("User-Agent", "Mozilla/5.0"); // 헤더 설정
                conn.connect();

                try (InputStream in = conn.getInputStream()) {
                    byte[] imageBytes = in.readAllBytes();

                    imageParts.add(Map.of(
                            "inlineData", Map.of(
                                    "mimeType", "image/jpeg",
                                    "data", Base64.getEncoder().encodeToString(imageBytes)
                            )
                    ));
                }
            } catch (Exception e) {
                System.out.println("이미지 URL 실패: " + accessUrl);
                e.printStackTrace();
                // TODO : 이미지 업로드 실패 시 error 처리
            }
        }

        // 프롬프트 구성
        String prompt =
                "아래 이미지는 의류의 앞/뒷면이며, 이 의류는 \"" + category + "\" 카테고리에 속한다.\n" +
                        "이 카테고리를 기준으로 손상 여부를 판단하고, 결과를 JSON 형식으로 반환한다.\n\n" +
                        "\n" +
                        "- result (검수결과): pass / hold / fail 중 하나를 선택\n" +
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
                        "  \"result\": \"pass | hold | fail\",\n" +
                        "  \"isTorn\": \"Y | N\",\n" +
                        "  \"hasStain\": \"Y | N\",\n" +
                        "  \"hasFading\": \"Y | N\",\n" +
                        "  \"isStretched\": \"Y | N\",\n" +
                        "  \"otherDefect\": \"Y | N\",\n" +
                        "  \"notes\": \"...\"\n" +
                        "}\n" +
                        "\n응답 결과는 코드 블록 없이 순수 JSON 형식으로만 출력해 주세요. notes 는 한글로 작성해 주세요.\n";

        Map<String, Object> textPart = Map.of("text", prompt);

        List<Map<String, Object>> parts = new ArrayList<>(imageParts);
        parts.add(textPart);

        Map<String, Object> requestBody = Map.of("contents", List.of(
                        Map.of("parts", parts)
                )
        );

        // HTTP 요청
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>(mapper.writeValueAsString(requestBody), headers);

        String fullUrl = apiUrl + "?key=" + apiKey;
        ResponseEntity<String> response = restTemplate.exchange(fullUrl, HttpMethod.POST, requestEntity, String.class);

        // 응답 파싱
        JsonNode root = mapper.readTree(response.getBody());
        String responseText = root
                .path("candidates").get(0)
                .path("content")
                .path("parts").get(0)
                .path("text").asText();

        // 코드 블록 제거
        String cleanedJson = responseText
                .replaceAll("(?i)```json", "")  // 대소문자 구분 없이 ```json 제거
                .replaceAll("```", "")         // 남은 백틱 제거
                .trim();                       // 앞뒤 공백 제거

        // 정제된 JSON 파싱
        JsonNode resultJson = mapper.readTree(cleanedJson);

        // 항목별 변수 추출
        String result = resultJson.path("result").asText().toUpperCase();
        String isTorn = resultJson.path("isTorn").asText().toUpperCase();
        String hasStain = resultJson.path("hasStain").asText().toUpperCase();
        String hasFading = resultJson.path("hasFading").asText().toUpperCase();
        String isStretched = resultJson.path("isStretched").asText().toUpperCase();
        String otherDefect = resultJson.path("otherDefect").asText().toUpperCase();
        String notes = resultJson.path("notes").asText();

        AiInspectorQueryDto aiInspectorQueryDto = AiInspectorQueryDto.builder()
                .productId(productId)
                .inspectMethod("AI")
                .notes(notes)
                .result(result)
                .isTorn(isTorn)
                .hasStain(hasStain)
                .hasFading(hasFading)
                .isStretched(isStretched)
                .otherDefect(otherDefect)
                .senderName(senderName)
                .build();

        // db 저장
        inspectorMapper.insert(aiInspectorQueryDto, productId);
    }

    // key 추출 메서드 추가
    private String extractKeyFromUrl(String accessUrl) {
        // "https://bucket.s3.region.amazonaws.com/uploads/abc.jpg" → "uploads/abc.jpg"
        return accessUrl.substring(accessUrl.indexOf("uploads"));
    }
}
