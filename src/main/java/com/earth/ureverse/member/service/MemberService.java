package com.earth.ureverse.member.service;

import com.earth.ureverse.member.dto.request.WithdrawRequestDto;

public interface MemberService {

    void withdraw(Long userId, WithdrawRequestDto withdrawRequestDto);

}
