package com.back.myboilerplate.domain.member.controller;

import com.back.myboilerplate.domain.member.dto.request.MemberCreateRequest;
import com.back.myboilerplate.domain.member.dto.request.MemberUpdateRequest;
import com.back.myboilerplate.domain.member.dto.response.MemberResponse;
import com.back.myboilerplate.domain.member.service.MemberGetService;
import com.back.myboilerplate.domain.member.service.MemberService;
import com.back.myboilerplate.global.common.response.ApiResponse;
import com.back.myboilerplate.global.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
@Tag(name = "Member", description = "회원 API")
public class MemberController implements MemberSwagger {
    private final MemberService memberService;
    private final MemberGetService memberGetService;

    @Override
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<MemberResponse> createMember(@RequestBody MemberCreateRequest request) {
        return ApiResponse.response(HttpStatus.CREATED, "회원가입 성공", memberService.createMember(request));
    }

    @Override
    @GetMapping("/me")
    public ApiResponse<MemberResponse> getMyInfo(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return ApiResponse.response(HttpStatus.OK, "조회 성공", memberGetService.getMemberResponse(userDetails.getMemberId()));
    }

    @Override
    @PatchMapping("/me")
    public ApiResponse<MemberResponse> updateMyInfo(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody MemberUpdateRequest request
    ) {
        return ApiResponse.response(HttpStatus.OK, "수정 성공", memberService.updateMember(userDetails.getMemberId(), request));
    }

    @Override
    @DeleteMapping("/me")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ApiResponse<Void> deleteMyInfo(@AuthenticationPrincipal CustomUserDetails userDetails) {
        memberService.deleteMember(userDetails.getMemberId());
        return ApiResponse.response(HttpStatus.NO_CONTENT, "탈퇴 성공");
    }
}
