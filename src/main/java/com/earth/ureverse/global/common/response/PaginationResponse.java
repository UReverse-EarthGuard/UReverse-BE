package com.earth.ureverse.global.common.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaginationResponse<T> {
    private List<T> items; //목록 데이터
    private long totalCount; //총 데이터 수
    private int pageNum; //현재 페이지 번호
    private int offset; //한 페이지에 보여줄 데이터 수
    private int totalPages; //전체 페이지 수
    private int startIndex; //데이터 시작 인덱스

    public PaginationResponse(List<T> items, long totalCount, int pageNum, int offset) {
        this.items = items;
        this.totalCount = totalCount;
        this.pageNum = pageNum;
        this.offset = offset;
        this.startIndex = (pageNum - 1) * offset;
        this.totalPages = (int) Math.ceil((double) totalCount / offset);
    }
}