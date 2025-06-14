package com.earth.ureverse.member.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberInfoResponseDto {

    private Long userId;
    private String name;
    private String email;
    private String phone;
    private String role;
    private Map<String, Integer> productStatus;

}
