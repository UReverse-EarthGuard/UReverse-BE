package com.earth.ureverse.global.auth.dto.db;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticatedUser {

    private Long id;    // PK
    private String username;
    private String password;
    private String role;

}
