package com.earth.ureverse.admin.service;


import com.earth.ureverse.admin.dto.request.ActiveMemberSearchRequest;
import com.earth.ureverse.admin.dto.response.ActiveMemberResponse;
import com.earth.ureverse.global.common.response.PaginationResponse;

import com.earth.ureverse.member.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class AdminUserServiceImpl implements AdminUserService {

    private final MemberMapper memberMapper;

    @Override
    public PaginationResponse<ActiveMemberResponse> getActiveUsers(ActiveMemberSearchRequest request) {
        List<ActiveMemberResponse> items = memberMapper.getActiveUsers(request);
        long total = memberMapper.countActiveUsers(request);
        return new PaginationResponse<>(items, total, request.getPageNum(), request.getOffset());
    }

}
