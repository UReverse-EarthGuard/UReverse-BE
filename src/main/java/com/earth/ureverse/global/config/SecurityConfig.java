package com.earth.ureverse.global.config;

import com.earth.ureverse.global.auth.JwtAuthenticationFilter;
import com.earth.ureverse.global.auth.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig implements WebMvcConfigurer {

    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .httpBasic(AbstractHttpConfigurer::disable)    // 기본 로그인 폼 비활성화
                .csrf(AbstractHttpConfigurer::disable)    // CSRF 비활성화
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)    // 세션 비활성화
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/v1/auth/**" ,    // 로그인, 회원가입
                                "/api/v1/oauth/**" ,
                                "/api/v1/home",
                                // Swagger
                                "/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**",
                                "/api-docs/**","/swagger-resources/**","/webjars/**","/configuration/**"
                        ).permitAll()
                        .requestMatchers("/api/v1/admins/**").hasRole("ADMIN")
                        .requestMatchers("/api/v1/inspectors/**").hasRole("INSPECTOR")
                        .requestMatchers("/api/v1/members/**").hasRole("MEMBER")
                        .requestMatchers("/api/v1/common/**").hasAnyRole("ADMIN", "INSPECTOR", "MEMBER")
                        .requestMatchers("/api/v1/notifications/subscribe/**").permitAll()
                        .anyRequest().authenticated()
                )
        ;
        http.addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        // SSE 구독 요청 - credentials 허용 X
        CorsConfiguration sseConfig = new CorsConfiguration();
        sseConfig.setAllowedOriginPatterns(List.of(
                "http://localhost:*", "https://localhost:*",
                "https://ureverse-fe-member.vercel.app",
                "https://ureverse-fe-corporate.vercel.app"
        ));
        sseConfig.setAllowedMethods(List.of("GET"));
        sseConfig.setAllowedHeaders(List.of("*"));
        sseConfig.setAllowCredentials(false); // EventSource는 credentials 사용 불가
        source.registerCorsConfiguration("/api/v1/notifications/subscribe/**", sseConfig);

        // 일반 API 요청 - credentials 허용 O
        CorsConfiguration apiConfig = new CorsConfiguration();
        apiConfig.setAllowedOriginPatterns(List.of(
                "http://localhost:*", "https://localhost:*",
                "https://ureverse-fe-member.vercel.app",
                "https://ureverse-fe-corporate.vercel.app"
        ));
        apiConfig.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        apiConfig.setAllowedHeaders(List.of("*"));
        apiConfig.setAllowCredentials(true); // 쿠키 및 인증 허용
        source.registerCorsConfiguration("/api/v1/**", apiConfig);

        return source;
    }

}
