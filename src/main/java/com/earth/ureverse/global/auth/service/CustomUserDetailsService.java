package com.earth.ureverse.global.auth.service;

import com.earth.ureverse.global.auth.CustomUserDetails;
import com.earth.ureverse.global.auth.dto.db.AuthenticatedUser;
import com.earth.ureverse.global.auth.mapper.AuthMapper;
import com.earth.ureverse.global.common.exception.InactiveUserException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final AuthMapper authMapper;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        AuthenticatedUser authenticatedUser = authMapper.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        if (!"active".equalsIgnoreCase(authenticatedUser.getIsActive())) {
            throw new InactiveUserException("탈퇴된 사용자입니다.");
        }

        return new CustomUserDetails(
                authenticatedUser.getUserId(),
                authenticatedUser.getEmail(),
                authenticatedUser.getPassword(),
                authenticatedUser.getRole()
        );
    }

}
