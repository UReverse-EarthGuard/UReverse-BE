package com.earth.ureverse.global.common.controller;

import com.earth.ureverse.global.common.dto.response.BrandResponseDto;
import com.earth.ureverse.global.common.dto.response.CategoryListResponseDto;
import com.earth.ureverse.global.common.response.CommonResponseEntity;
import com.earth.ureverse.global.common.service.CommonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Common", description = "공통 응답 포맷의 예제를 제공합니다. 성공 및 에러 응답 구조를 확인할 수 있습니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/common")
public class CommonController {

    private final CommonService commonService;

    //성공
    @Operation(summary = "성공 예시", description = "성공 응답 예제입니다.")
    @ApiResponse(responseCode = "200", description = "성공 응답",
            content = @Content(schema = @Schema(implementation = String.class)))
    @GetMapping("/success")
    public CommonResponseEntity<String> getSuccessExample() {
        String data = "This is a successful response";
        return CommonResponseEntity.success(data);
    }

    //에러
    @Operation(summary = "에러 예시", description = "에러 응답 예제입니다.")
    @ApiResponse(responseCode = "500", description = "에러 응답",
            content = @Content(schema = @Schema(implementation = String.class)))
    @GetMapping("/error")
    public ResponseEntity<CommonResponseEntity<Object>> getErrorExample() {
        CommonResponseEntity<Object> body =
                CommonResponseEntity.error(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred");
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/allBrands")
    public CommonResponseEntity<List<BrandResponseDto>> getAllBrand(){
        List<BrandResponseDto> brandResponseDtos = commonService.getBrands();
        return CommonResponseEntity.success(brandResponseDtos);
    }

    @GetMapping("/allCategoryMainName")
    public CommonResponseEntity<List<String>> getAllCategory(){
        List<String> categoryResponseDtos = commonService.getCategories();
        return CommonResponseEntity.success(categoryResponseDtos);
    }

    @GetMapping("/brands/{brandId}/categories")
    public CommonResponseEntity<List<CategoryListResponseDto>> getCategoryInBrand(@PathVariable Long brandId){
        List<CategoryListResponseDto> categoryResponseDtos = commonService.getCategoriesByBrandId(brandId);
        return CommonResponseEntity.success(categoryResponseDtos);
    }
}
