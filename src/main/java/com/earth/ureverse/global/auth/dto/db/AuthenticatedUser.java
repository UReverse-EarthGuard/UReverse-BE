package com.earth.ureverse.global.auth.dto.db;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticatedUser {

    private Long userId;    // PK
    private String email;
    private String password;
    private String role;
    private String isActive;
    private String kakaoId;
}
