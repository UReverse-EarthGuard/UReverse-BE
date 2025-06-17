package com.earth.ureverse.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {

        // SSE 연결용. credentials 비허용
        registry.addMapping("/api/v1/notifications/subscribe/**")
                .allowedOrigins("http://localhost:3000", "https://localhost:3000")
                .allowedMethods("GET")
                .allowedHeaders("*")
                .allowCredentials(false); // EventSource는 credentials 못 씀

        registry.addMapping("/api/v1/**")
                .allowedOriginPatterns("http://localhost:*", "https://localhost:*")
                .allowedMethods("GET", "POST", "PATCH", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);    // 쿠키 전송 허용
    }
}