package com.back.myboilerplate.domain.member.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Role {
    MEMBER("ROLE_MEMBER"),
    ADMIN("ROLE_ADMIN");
    private final String authority;
}
