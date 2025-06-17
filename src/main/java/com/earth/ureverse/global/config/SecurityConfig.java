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

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

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

}
