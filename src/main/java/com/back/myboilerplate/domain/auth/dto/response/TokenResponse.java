package com.back.myboilerplate.domain.auth.dto.response;

public record TokenResponse(
        String accessToken,
        String refreshToken
) {}
