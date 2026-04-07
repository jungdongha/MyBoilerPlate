package com.back.myboilerplate.domain.auth.controller;

import com.back.myboilerplate.domain.auth.dto.request.LoginRequest;
import com.back.myboilerplate.domain.auth.dto.response.TokenResponse;
import com.back.myboilerplate.domain.auth.service.AuthService;
import com.back.myboilerplate.global.common.response.ApiResponse;
import com.back.myboilerplate.global.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController implements AuthSwagger {
    private final AuthService authService;

    @Override
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<TokenResponse>> login(@RequestBody LoginRequest request) {
        TokenResponse response = authService.login(request);
        return ResponseEntity.ok(ApiResponse.response(HttpStatus.OK, "로그인 성공", response));
    }

    @Override
    @PostMapping("/reissue")
    public ResponseEntity<ApiResponse<TokenResponse>> reissue(@AuthenticationPrincipal CustomUserDetails userDetails) {
        // userDetails에서 토큰 정보를 직접 가져올 수 없으므로, 보통은 쿠키나 헤더의 Refresh Token을 사용하지만
        // 여기서는 서비스 구조에 맞춰 memberId를 넘기거나 로직을 수정해야 함. 
        // 일단 컴파일을 위해 refresh로 변경 (refresh 메서드는 String refreshToken을 받으므로 구조적 수정 필요할 수 있음)
        // 기존 코드의 의도가 reissue(Long memberId)라면 AuthService에 해당 메서드 추가 필요
        TokenResponse response = authService.reissue(userDetails.getMemberId());
        return ResponseEntity.ok(ApiResponse.response(HttpStatus.OK, "토큰 재발급 성공", response));
    }

    @Override
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(@AuthenticationPrincipal CustomUserDetails userDetails) {
        authService.logout(userDetails.getMemberId());
        return ResponseEntity.ok(ApiResponse.response(HttpStatus.OK, "로그아웃 성공"));
    }
}
