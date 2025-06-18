package com.earth.ureverse.member.dto.request;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProductUploadRequestDto {
    private String address;
    private String addressDetail;
    private String name;
    private String phone;
    private String zipCode;
    private String brandId;
    private String brandName;
    private String categoryId;
    private String category;
    private List<String> productsImageUrl;
}
