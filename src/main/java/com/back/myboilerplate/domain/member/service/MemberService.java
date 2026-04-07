package com.back.myboilerplate.domain.member.service;

import com.back.myboilerplate.domain.member.dto.request.MemberCreateRequest;
import com.back.myboilerplate.domain.member.dto.request.MemberUpdateRequest;
import com.back.myboilerplate.domain.member.dto.response.MemberResponse;
import com.back.myboilerplate.domain.member.entity.Member;
import com.back.myboilerplate.domain.member.entity.Role;
import com.back.myboilerplate.domain.member.exception.MemberException;
import com.back.myboilerplate.domain.member.exception.MemberExceptionInformation;
import com.back.myboilerplate.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final MemberGetService memberGetService;

    public MemberResponse createMember(MemberCreateRequest request) {
        if (memberRepository.existsByEmail(request.email())) {
            throw new MemberException(MemberExceptionInformation.DUPLICATE_EMAIL);
        }
        Member member = Member.builder()
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .nickName(request.nickname())
                .role(Role.MEMBER)
                .build();
        return MemberResponse.from(memberRepository.save(member));
    }

    public MemberResponse updateMember(Long id, MemberUpdateRequest request) {
        Member member = memberGetService.getMember(id);
        member.updateMember(request.nickname());
        return MemberResponse.from(member);
    }

    public void deleteMember(Long id) {
        Member member = memberGetService.getMember(id);
        memberRepository.delete(member);
    }
}

