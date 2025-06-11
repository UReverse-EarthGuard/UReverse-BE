package com.earth.ureverse.admin.service;

import com.earth.ureverse.admin.dto.response.FinishProductResponse;

import java.util.List;

public interface AdminProductService {
    List<FinishProductResponse> getFinishProducts();
}
