package com.earth.ureverse.member.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PointHistoryListResponseDto {

    private int totalPoint;
    private List<PointHistoryResponseDto> pointHistory;
    private String lastCreatedAt;    // 이전 페이지에서 마지막으로 조회된 데이터의 createdAt
    private Long lastProductId;    // 이전 페이지에서 마지막으로 조회된 데이터의 productId

}