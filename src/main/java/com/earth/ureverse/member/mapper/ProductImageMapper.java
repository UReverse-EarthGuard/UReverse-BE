package com.earth.ureverse.member.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.query.Param;

@Mapper
public interface ProductImageMapper {
    boolean existsByUrl(@Param("url") String url);

    void insertProductImage(@Param("productId") Long productId, @Param("url") String url, @Param("userId") Long userId);
}
