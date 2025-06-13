package com.earth.ureverse.member.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class ChangePasswordRequestDto {

    @NotBlank
    private String currentPassword;

    @NotBlank
    private String newPassword;

}
