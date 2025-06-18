package com.earth.ureverse.admin.dto.query;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductNotificationInfoDto {
    private Long productId;
    private Long userId;
    private String brand;
    private String category;
}
