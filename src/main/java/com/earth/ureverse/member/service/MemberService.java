package com.earth.ureverse.member.service;

import com.earth.ureverse.member.dto.request.ChangePasswordRequestDto;
import com.earth.ureverse.member.dto.request.UpdateMemberRequestDto;
import com.earth.ureverse.member.dto.request.WithdrawRequestDto;
import jakarta.validation.Valid;

public interface MemberService {

    void withdraw(Long userId, WithdrawRequestDto withdrawRequestDto);

    void updateMember(Long userId, @Valid UpdateMemberRequestDto updateMemberRequestDto);

    void changePassword(Long userId, @Valid ChangePasswordRequestDto changePasswordRequestDto);

}
