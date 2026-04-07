package com.back.myboilerplate.domain.member.controller;

import com.back.myboilerplate.domain.member.dto.request.MemberCreateRequest;
import com.back.myboilerplate.domain.member.dto.request.MemberUpdateRequest;
import com.back.myboilerplate.domain.member.dto.response.MemberResponse;
import com.back.myboilerplate.global.common.response.ApiResponse;
import com.back.myboilerplate.global.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Member", description = "회원 API")
public interface MemberSwagger {

    @Operation(summary = "회원가입", description = "새로운 회원을 등록합니다.")
    ApiResponse<MemberResponse> createMember(@RequestBody MemberCreateRequest request);

    @Operation(summary = "내 정보 조회", description = "현재 로그인한 회원의 정보를 조회합니다.")
    ApiResponse<MemberResponse> getMyInfo(@AuthenticationPrincipal CustomUserDetails userDetails);

    @Operation(summary = "내 정보 수정", description = "현재 로그인한 회원의 정보를 수정합니다.")
    ApiResponse<MemberResponse> updateMyInfo(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody MemberUpdateRequest request
    );

    @Operation(summary = "회원 탈퇴", description = "현재 로그인한 회원을 삭제합니다.")
    ApiResponse<Void> deleteMyInfo(@AuthenticationPrincipal CustomUserDetails userDetails);
}
