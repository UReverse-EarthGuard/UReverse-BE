package com.earth.ureverse.admin.service;

import com.earth.ureverse.admin.dto.request.ActiveMemberSearchRequest;
import com.earth.ureverse.admin.dto.response.ActiveMemberResponse;
import com.earth.ureverse.global.common.response.PaginationResponse;

public interface AdminUserService {

    PaginationResponse<ActiveMemberResponse> getActiveUsers(ActiveMemberSearchRequest request);
}
