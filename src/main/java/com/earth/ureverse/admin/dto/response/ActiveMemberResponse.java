package com.earth.ureverse.admin.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ActiveMemberResponse {
    private Long id; //userId
    private String name; //userName
    private String email;
    private String phone;
    private Long point;
    private Integer sales;
    private String createdAt; //가입 일자
}
