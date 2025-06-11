package com.earth.ureverse.global.auth;

import com.earth.ureverse.global.common.exception.TokenExpiredException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;

// JWT 생성, 파싱, 유효성 검사
@Component
public class JwtTokenProvider {

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.access-token-valid-time}")
    private long accessTokenValidTime;

    @Value("${jwt.refresh-token-valid-time}")
    private long refreshTokenValidTime;

    private SecretKey key;
    private JwtParser jwtParser;

    @PostConstruct
    public void init() {    // Bean 초기화 시, secret key를 Base64 인코딩하고 Key 객체로 변환
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.jwtParser = Jwts.parser().verifyWith(key).build();
    }

    // Access Token 생성(권한 포함)
    public String generateAccessToken(String email, List<String> roles) {
        return generateToken(email, roles, accessTokenValidTime, "access");
    }

    // Refresh Token 생성(권한 포함X)
    public String generateRefreshToken(String email) {
        return generateToken(email, null, refreshTokenValidTime, "refresh");
    }

    private String generateToken(String email, List<String> roles, long validTime, String type) {
        Date now = new Date();
        Date expirationTime = new Date(now.getTime() + validTime);

        return Jwts.builder()
                .subject(email)
                .issuedAt(now)
                .expiration(expirationTime)
                .claim("roles", roles)
                .claim("type", type)    // access, refresh
                .signWith(key, Jwts.SIG.HS256)
                .compact();
    }

    public String getEmailFromToken(String token) {
        return jwtParser.parseSignedClaims(token).getPayload().getSubject();
    }

    public List<String> getRolesFromToken(String token) {
        Object rawRoles = jwtParser.parseSignedClaims(token).getPayload().get("roles");
        if (rawRoles instanceof List<?>) {
            return ((List<?>) rawRoles).stream()
                    .map(Object::toString)
                    .toList();
        }
        return List.of();
    }

    public String getTokenType(String token) {
        return jwtParser.parseSignedClaims(token).getPayload().get("type", String.class);
    }

    // 토큰 유효성 검증(서명, 만료 체크)
    public boolean validateToken(String token) {
        try {
            Claims claims = jwtParser.parseSignedClaims(token).getPayload();

            if (claims.getExpiration().before(new Date())) {
                throw new TokenExpiredException("토큰이 만료되었습니다.");
            }
            return true;
        } catch (JwtException e) {
            throw new TokenExpiredException("유효하지 않은 토큰입니다.");
        }
    }

    // 쿠키에서 refreshToken을 꺼내오는 메서드
    public String resolveRefreshToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refreshToken".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

}
