package com.earth.ureverse.global.auth.service;

import com.earth.ureverse.global.auth.CustomUserDetails;
import com.earth.ureverse.global.auth.JwtTokenProvider;
import com.earth.ureverse.global.auth.dto.db.AuthenticatedUser;
import com.earth.ureverse.global.auth.dto.request.LoginRequestDto;
import com.earth.ureverse.global.auth.dto.request.RecoveryPasswordRequestDto;
import com.earth.ureverse.global.auth.dto.request.SignUpRequestDto;
import com.earth.ureverse.global.auth.dto.response.LoginResponseDto;
import com.earth.ureverse.global.auth.mapper.AuthMapper;
import com.earth.ureverse.global.common.exception.IllegalParameterException;
import com.earth.ureverse.global.common.exception.TokenExpiredException;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthMapper authMapper;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;

    @Override
    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDto.getEmail(), loginRequestDto.getPassword())
        );

        List<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        AuthenticatedUser authenticatedUser = authMapper.findByEmail(loginRequestDto.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        String accessToken = jwtTokenProvider.generateAccessToken(authenticatedUser.getUserId(), authenticatedUser.getEmail(), roles);
        String refreshToken = jwtTokenProvider.generateRefreshToken(authenticatedUser.getUserId(), authenticatedUser.getEmail());
        String role = authenticatedUser.getRole();

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

        Long userId = jwtTokenProvider.getUserIdFromToken(refreshToken);
        AuthenticatedUser authenticatedUser = authMapper.findByUserId(userId)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
        String newAccessToken = jwtTokenProvider.generateAccessToken(
                authenticatedUser.getUserId(), authenticatedUser.getEmail(), List.of(authenticatedUser.getRole())
        );

        return new LoginResponseDto(newAccessToken, null, authenticatedUser.getRole());
    }

    @Override
    public void signUp(SignUpRequestDto signUpRequestDto) {
        if (authMapper.existsByEmail(signUpRequestDto.getEmail())) {
            throw new IllegalParameterException("이미 존재하는 이메일입니다.");
        }

        String encodedPassword = passwordEncoder.encode(signUpRequestDto.getPassword());
        signUpRequestDto.setPassword(encodedPassword);
        authMapper.insertMember(signUpRequestDto);
    }

    @Override
    public String getUserName(CustomUserDetails customUserDetails) {
        Long userId = customUserDetails.getUserId();
        return authMapper.getUserName(userId);
    }

    @Override
    public void recoverPassword(RecoveryPasswordRequestDto recoveryPasswordRequestDto) {
        String email = recoveryPasswordRequestDto.getEmail();

        if(!authMapper.existsByEmail(email)) {
            throw new UsernameNotFoundException("존재하지 않는 이메일입니다.");
        }

        String tempPassword = generateTempPassword();
        String encoded = passwordEncoder.encode(tempPassword);

        authMapper.updatePasswordByEmail(email, encoded);
        sendTempPasswordEmail(email, tempPassword);
    }

    private String generateTempPassword() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom random = new SecureRandom();
        return random.ints(10, 0, chars.length())
                .mapToObj(i -> String.valueOf(chars.charAt(i)))
                .collect(Collectors.joining());
    }

    private void sendTempPasswordEmail(String email, String tempPassword) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("[U:Reverse] 임시 비밀번호 발송");
        message.setText("임시 비밀번호: " + tempPassword + "\n로그인 후 반드시 비밀번호를 변경해주세요.");
        mailSender.send(message);
    }

}
