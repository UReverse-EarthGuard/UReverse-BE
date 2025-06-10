package com.earth.ureverse.global.auth.service;

import com.earth.ureverse.global.auth.JwtTokenProvider;
import com.earth.ureverse.global.auth.dto.db.AuthenticatedUser;
import com.earth.ureverse.global.auth.dto.request.LoginRequestDto;
import com.earth.ureverse.global.auth.dto.response.LoginResponseDto;
import com.earth.ureverse.global.auth.mapper.AuthMapper;
import com.earth.ureverse.global.common.exception.TokenExpiredException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthMapper authMapper;

    @Override
    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDto.getUsername(), loginRequestDto.getPassword())
        );

        List<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        String accessToken = jwtTokenProvider.generateAccessToken(loginRequestDto.getUsername(), roles);
        String refreshToken = jwtTokenProvider.generateRefreshToken(loginRequestDto.getUsername());
        String role = authentication.getAuthorities().iterator().next().getAuthority();

        return new LoginResponseDto(accessToken, refreshToken, role);
    }

    @Override
    public LoginResponseDto refreshAccessToken(String refreshToken) {
        if (refreshToken == null || refreshToken.isBlank()) {
            throw new TokenExpiredException("Refresh Token이 존재하지 않습니다.");
        }

        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new TokenExpiredException("유효하지 않은 Refresh Token입니다.");
        }

        String username = jwtTokenProvider.getUsernameFromToken(refreshToken);
        AuthenticatedUser user = authMapper.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
        String newAccessToken = jwtTokenProvider.generateAccessToken(user.getUsername(), List.of(user.getRole()));

        return new LoginResponseDto(newAccessToken, null, user.getRole());
    }

}
