package com.back.myboilerplate.global.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.ai.groq")
public record AiProperties(
        String apiKey,
        String baseUrl,
        String model,
        int maxTokens
) {}
