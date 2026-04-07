package com.back.myboilerplate.global.security.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RefreshTokenRepository {

    private static final String KEY_PREFIX = "RT:";
    private final StringRedisTemplate stringRedisTemplate;

    public void save(Long memberId, String refreshToken, Duration ttl) {
        stringRedisTemplate.opsForValue().set(key(memberId), refreshToken, ttl);
    }

    public Optional<String> find(Long memberId) {
        return Optional.ofNullable(stringRedisTemplate.opsForValue().get(key(memberId)));
    }

    public void delete(Long memberId) {
        stringRedisTemplate.delete(key(memberId));
    }

    private String key(Long memberId) {
        return KEY_PREFIX + memberId;
    }
}
