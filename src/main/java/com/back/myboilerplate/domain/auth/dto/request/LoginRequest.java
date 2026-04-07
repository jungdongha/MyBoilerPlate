package com.back.myboilerplate.domain.auth.dto.request;

public record LoginRequest(
        String email,
        String password
) {}
