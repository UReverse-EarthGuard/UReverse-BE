package com.earth.ureverse.global.auth.mapper;

import com.earth.ureverse.global.auth.dto.db.AuthenticatedUser;
import com.earth.ureverse.global.auth.dto.request.SignUpRequestDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;

@Mapper
public interface AuthMapper {

    Optional<AuthenticatedUser> findByEmail(@Param(value="email") String email);

    boolean existsByEmail(@Param(value = "email") String email);

    void insertMember(SignUpRequestDto signupRequestDto);

    Optional<AuthenticatedUser> findByUserId(@Param(value="userId") Long userId);

    void updateIsActive(@Param("userId") Long userId, @Param("isActive") String inactive);

    String getUserName(Long userId);

    void updatePasswordByEmail(@Param(value = "email") String email, @Param(value = "encodedPassword") String encodedPassword);

}
