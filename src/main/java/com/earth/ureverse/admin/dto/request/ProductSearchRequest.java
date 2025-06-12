package com.earth.ureverse.admin.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductSearchRequest {
    private String brand;
    private String categoryMain;
    private String categorySub;
    private String grade;
    private int offset;    // 페이지당 개수
    private int pageNum;   // 현재 페이지 번호

    public int getPageStart() {
        return (pageNum - 1) * offset;
    }
}
