package com.back.myboilerplate.domain.auth.controller;

import com.back.myboilerplate.domain.auth.dto.request.LoginRequest;
import com.back.myboilerplate.domain.auth.dto.response.TokenResponse;
import com.back.myboilerplate.global.common.response.ApiResponse;
import com.back.myboilerplate.global.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Auth", description = "인증 API")
public interface AuthSwagger {

    @Operation(summary = "로그인", description = "이메일과 비밀번호로 로그인합니다.")
    ResponseEntity<ApiResponse<TokenResponse>> login(@RequestBody LoginRequest request);

    @Operation(summary = "토큰 재발급", description = "Refresh Token으로 새로운 Access Token을 발급받습니다.")
    ResponseEntity<ApiResponse<TokenResponse>> reissue(@AuthenticationPrincipal CustomUserDetails userDetails);

    @Operation(summary = "로그아웃", description = "Refresh Token을 무효화합니다.")
    ResponseEntity<ApiResponse<Void>> logout(@AuthenticationPrincipal CustomUserDetails userDetails);
}
