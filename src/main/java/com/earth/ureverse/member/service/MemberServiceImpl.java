package com.earth.ureverse.member.service;

import com.earth.ureverse.global.auth.dto.db.AuthenticatedUser;
import com.earth.ureverse.global.auth.mapper.AuthMapper;
import com.earth.ureverse.global.common.exception.AlreadyWithdrawnException;
import com.earth.ureverse.global.common.exception.IllegalParameterException;
import com.earth.ureverse.global.common.exception.PasswordMismatchException;
import com.earth.ureverse.member.dto.request.UpdateMemberRequestDto;
import com.earth.ureverse.member.dto.request.WithdrawRequestDto;
import com.earth.ureverse.member.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final AuthMapper authMapper;
    private final MemberMapper memberMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void withdraw(Long userId, WithdrawRequestDto withdrawRequestDto) {
        AuthenticatedUser authenticatedUser = authMapper.findByUserId(userId)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        if (!"active".equalsIgnoreCase(authenticatedUser.getIsActive())) {
            throw new AlreadyWithdrawnException();
        }

        if (!passwordEncoder.matches(withdrawRequestDto.getPassword(), authenticatedUser.getPassword())) {
            throw new PasswordMismatchException();
        }

        memberMapper.updateIsActive(userId, "inactive");
    }

    @Override
    public void updateMember(Long userId, UpdateMemberRequestDto updateMemberRequestDto) {
        AuthenticatedUser authenticatedUser = authMapper.findByUserId(userId)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        if (!"active".equalsIgnoreCase(authenticatedUser.getIsActive())) {
            throw new AlreadyWithdrawnException();
        }

        // 이메일 변경 시 비밀번호 확인 + 중복 체크
        if (updateMemberRequestDto.getEmail() != null && !updateMemberRequestDto.getEmail().equals(authenticatedUser.getEmail())) {
            if (updateMemberRequestDto.getPassword() == null ||
                    !passwordEncoder.matches(updateMemberRequestDto.getPassword(), authenticatedUser.getPassword())) {
                throw new PasswordMismatchException();
            }

            if (authMapper.existsByEmail(updateMemberRequestDto.getEmail())) {
                throw new IllegalParameterException("이미 존재하는 이메일입니다.");
            }
        }

        memberMapper.updateMember(userId, updateMemberRequestDto);
    }

}
