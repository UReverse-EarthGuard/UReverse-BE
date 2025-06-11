package com.earth.ureverse.member.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class WithdrawRequestDto {

    @NotBlank(message = "비밀번호는 필수입니다.")
    private String password;

}
