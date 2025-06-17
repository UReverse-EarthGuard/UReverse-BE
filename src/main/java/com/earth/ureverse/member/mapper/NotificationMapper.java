package com.earth.ureverse.member.mapper;

import com.earth.ureverse.member.dto.query.NotificationQueryDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface NotificationMapper {
    void insert(NotificationQueryDto queryDto);
}
