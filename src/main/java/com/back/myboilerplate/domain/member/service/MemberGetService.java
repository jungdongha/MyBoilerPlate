package com.back.myboilerplate.domain.member.service;

import com.back.myboilerplate.domain.member.dto.response.MemberResponse;
import com.back.myboilerplate.domain.member.entity.Member;
import com.back.myboilerplate.domain.member.exception.MemberException;
import com.back.myboilerplate.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.back.myboilerplate.domain.member.exception.MemberExceptionInformation.MEMBER_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberGetService {
    private final MemberRepository memberRepository;

    public Member getMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));
    }

    public MemberResponse getMemberResponse(Long memberId) {
        return MemberResponse.from(getMember(memberId));
    }

    // 추가 쿼리 로직들
}
