package com.earth.ureverse.member.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class UpdateMemberRequestDto {

    private String name;

    @Pattern(
            regexp = "^010-\\d{4}-\\d{4}$",
            message = "휴대폰 번호 형식이 올바르지 않습니다. 예: 010-1234-5678"
    )
    private String phone;

    @Email
    private String email;

    private String password;

}
