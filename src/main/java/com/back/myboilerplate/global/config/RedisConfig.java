package com.back.myboilerplate.global.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

@Configuration
public class RedisConfig {

    /**
     *
     * * 현재는 JWT를 위해 stringResdisTemplate 사용
     * * 다양한 객체 사용하려면 RedisTemplate 필요
     *
     */
    @Bean
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        return new StringRedisTemplate(redisConnectionFactory);
    }
}
