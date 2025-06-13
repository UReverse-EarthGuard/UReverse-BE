package com.earth.ureverse.global.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(title = "U:Reverse API 명세서", version = "v1", description = "U:Reverse API 명세서입니다."),
        security = @SecurityRequirement(name = "Bearer Authentication"), // 🔐 모든 API에 적용될 기본 인증 설정
        servers = {
                @Server(
                        description = "로컬 서버",
                        url = "http://localhost:8080"
                ),
                @Server(
                        description = "운영 서버",
                        url = "http://13.125.63.234:8080"
                )
        }
)
@SecurityScheme(
        name = "Bearer Authentication",                  // 위에서 지정한 인증 이름
        description = "JWT 토큰을 입력하세요 (Bearer {token})",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class SwaggerConfig {
    @Bean
    public GroupedOpenApi chatOpenApi() {
        // "/v1/**" 경로에 매칭되는 API를 그룹화하여 문서화한다.
        String[] paths = {"/api/v1/**"};

        return GroupedOpenApi.builder()
                .group("ureverse-api-v1")  // 그룹 이름을 설정한다.
                .pathsToMatch(paths)     // 그룹에 속하는 경로 패턴을 지정한다.
                .build();
    }
}