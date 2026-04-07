package com.back.myboilerplate.domain.ai.dto.request;

public record ChatRequest(
        String message,
        String conversationId,
        String model

) {
}
