package com.earth.ureverse.member.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SalesHistoryListResponseDto {

    private List<SalesHistoryResponseDto> histories;
    private String lastCreatedAt;    // 이전 페이지에서 마지막으로 조회된 데이터의 createdAt
    private Long lastProductId;    // 이전 페이지에서 마지막으로 조회된 데이터의 productId

}
