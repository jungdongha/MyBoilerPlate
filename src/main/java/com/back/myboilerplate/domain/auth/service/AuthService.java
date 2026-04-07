package com.back.myboilerplate.domain.auth.service;

import com.back.myboilerplate.domain.auth.dto.request.LoginRequest;
import com.back.myboilerplate.domain.auth.dto.response.TokenResponse;
import com.back.myboilerplate.domain.auth.exception.AuthException;
import com.back.myboilerplate.domain.member.entity.Member;
import com.back.myboilerplate.domain.member.exception.MemberException;
import com.back.myboilerplate.domain.member.repository.MemberRepository;
import com.back.myboilerplate.global.properties.JwtProperties;
import com.back.myboilerplate.global.security.jwt.JwtProvider;
import com.back.myboilerplate.global.security.redis.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;

import static com.back.myboilerplate.domain.auth.exception.AuthExceptionInformation.*;
import static com.back.myboilerplate.domain.member.exception.MemberExceptionInformation.MEMBER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtProperties jwtProperties;

    public TokenResponse login(LoginRequest request) {
        Member member = memberRepository.findByEmail(request.email())
                .orElseThrow(() -> new AuthException(INVALID_CREDENTIALS));

        if (!passwordEncoder.matches(request.password(), member.getPassword())) {
            throw new AuthException(INVALID_CREDENTIALS);
        }

        String accessToken = jwtProvider.generateAccessToken(member.getId(), member.getEmail(), member.getRole());
        String refreshToken = jwtProvider.generateRefreshToken(member.getId());

        refreshTokenRepository.save(member.getId(), refreshToken,
                Duration.ofMillis(jwtProperties.refreshExpiration()));

        return new TokenResponse(accessToken, refreshToken);
    }

    public TokenResponse refresh(String refreshToken) {
        if (!jwtProvider.validateToken(refreshToken)) {
            throw new AuthException(INVALID_TOKEN);
        }

        Long memberId = jwtProvider.getMemberId(refreshToken);

        String storedToken = refreshTokenRepository.find(memberId)
                .orElseThrow(() -> new AuthException(TOKEN_NOT_FOUND));

        if (!storedToken.equals(refreshToken)) {
            refreshTokenRepository.delete(memberId);
            throw new AuthException(TOKEN_MISMATCH);
        }

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));

        String newAccessToken = jwtProvider.generateAccessToken(member.getId(), member.getEmail(), member.getRole());
        String newRefreshToken = jwtProvider.generateRefreshToken(member.getId());

        refreshTokenRepository.save(memberId, newRefreshToken,
                Duration.ofMillis(jwtProperties.refreshExpiration()));

        return new TokenResponse(newAccessToken, newRefreshToken);
    }

    public TokenResponse reissue(Long memberId) {
        String storedToken = refreshTokenRepository.find(memberId)
                .orElseThrow(() -> new AuthException(TOKEN_NOT_FOUND));

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));

        String newAccessToken = jwtProvider.generateAccessToken(member.getId(), member.getEmail(), member.getRole());
        String newRefreshToken = jwtProvider.generateRefreshToken(member.getId());

        refreshTokenRepository.save(memberId, newRefreshToken,
                Duration.ofMillis(jwtProperties.refreshExpiration()));

        return new TokenResponse(newAccessToken, newRefreshToken);
    }

    public void logout(Long memberId) {
        refreshTokenRepository.find(memberId)
                .orElseThrow(() -> new AuthException(ALREADY_LOGGED_OUT));
        refreshTokenRepository.delete(memberId);
    }
}

