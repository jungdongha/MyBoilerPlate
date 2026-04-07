package com.back.myboilerplate.domain.member.dto.request;

import jakarta.validation.constraints.NotBlank;

public record MemberUpdateRequest(
        @NotBlank(message = "닉네임은 필수입니다")
        String nickname
) {
}
