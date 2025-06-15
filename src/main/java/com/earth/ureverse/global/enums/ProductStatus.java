package com.earth.ureverse.global.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProductStatus {

    REGISTER("상품 등록"),
    FIRST_INSPECT("1차 검수"),
    SECOND_INSPECT("2차 검수"),
    DELIVERY_REQUEST("배송 요청 등록"),
    DELIVERING("배송 중"),
    FINISH("배송 완료");

    private final String label;

    // 역매핑용 (DB 문자열 → enum)
    public static String getLabelByCode(String code) {
        for (ProductStatus status : values()) {
            if (status.name().equalsIgnoreCase(code)) {
                return status.getLabel();
            }
        }
        return code;
    }

}
