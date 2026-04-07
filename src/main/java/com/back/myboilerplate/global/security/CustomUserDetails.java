package com.back.myboilerplate.global.security;

import com.back.myboilerplate.domain.member.entity.Member;
import com.back.myboilerplate.domain.member.entity.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {

    private final Long memberId;
    private final String email;
    private final String password;
    private final Role role;

    public CustomUserDetails(Member member) {
        this.memberId = member.getId();
        this.email = member.getEmail();
        this.password = member.getPassword();
        this.role = member.getRole();
    }

    public static CustomUserDetails fromToken(Long memberId, String email, Role role) {
        return new CustomUserDetails(memberId, email, null, role);
    }

    private CustomUserDetails(Long memberId, String email, String password, Role role) {
        this.memberId = memberId;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public Long getMemberId() {
        return memberId;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.getAuthority()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

