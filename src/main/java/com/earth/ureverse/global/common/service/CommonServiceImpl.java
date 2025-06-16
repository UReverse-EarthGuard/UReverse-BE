package com.earth.ureverse.global.common.service;

import com.earth.ureverse.global.common.dto.response.BrandResponseDto;
import com.earth.ureverse.global.common.mapper.BrandMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommonServiceImpl implements CommonService {

    private final BrandMapper brandMapper;

    @Override
    public List<BrandResponseDto> getBrands() {
        log.info("getBrands()");
        List<BrandResponseDto> brandResponseDtos = brandMapper.selectAllBrands();
        log.info("getBrands() :: brandResponseDtos.size() = " + brandResponseDtos.size());
        return brandResponseDtos;
    }
}
