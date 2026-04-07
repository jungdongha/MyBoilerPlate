package com.back.myboilerplate.global.security;

import com.back.myboilerplate.domain.member.entity.Member;
import com.back.myboilerplate.domain.member.exception.MemberException;
import com.back.myboilerplate.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.back.myboilerplate.domain.member.exception.MemberExceptionInformation.MEMBER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));
        return new CustomUserDetails(member);
    }
}

