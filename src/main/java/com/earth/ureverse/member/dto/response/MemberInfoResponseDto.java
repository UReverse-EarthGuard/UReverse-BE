package com.earth.ureverse.member.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberInfoResponseDto {

    private Long userId;
    private String name;
    private String email;
    private String phone;
    private String role;

}
