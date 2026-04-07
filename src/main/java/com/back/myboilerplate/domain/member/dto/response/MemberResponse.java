package com.back.myboilerplate.domain.member.dto.response;

import com.back.myboilerplate.domain.member.entity.Member;
import com.back.myboilerplate.domain.member.entity.Role;

import java.time.LocalDateTime;

public record MemberResponse(
        Long id,
        String email,
        String nickname,
        Role role,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static MemberResponse from(Member member) {
        return new MemberResponse(
                member.getId(),
                member.getEmail(),
                member.getNickName(),
                member.getRole(),
                member.getCreatedAt(),
                member.getUpdatedAt()
        );
    }
}

