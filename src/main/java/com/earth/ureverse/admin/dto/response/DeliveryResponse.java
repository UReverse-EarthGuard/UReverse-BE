package com.earth.ureverse.admin.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryResponse {
    private String senderName; //판매자 이름
    private Integer postalCode; //우편번호
    private String address; //주소
    private String senderPhone; //판매자 전화번호
}
