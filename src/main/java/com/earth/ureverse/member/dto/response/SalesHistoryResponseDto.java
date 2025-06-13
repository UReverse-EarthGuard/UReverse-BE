package com.earth.ureverse.member.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SalesHistoryResponseDto {

    private int point;
    private Long productId;
    private String brandName;
    private String categoryMainName;
    private String categorySubName;
    private String status;
    private String createdAt;

}
