package com.earth.ureverse.inspector.mapper;

import com.earth.ureverse.inspector.dto.query.AiInspectorQueryDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface InspectorMapper {
    void insert(@Param("dto") AiInspectorQueryDto queryDto, @Param("productId") Long productId);
}
