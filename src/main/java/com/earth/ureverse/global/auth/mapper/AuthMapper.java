package com.earth.ureverse.global.auth.mapper;

import com.earth.ureverse.global.auth.dto.db.AuthenticatedUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;

@Mapper
public interface AuthMapper {

    Optional<AuthenticatedUser> findByUsername(@Param(value="username") String username);

}
