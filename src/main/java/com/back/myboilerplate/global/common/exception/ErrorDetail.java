package com.back.myboilerplate.global.common.exception;

import lombok.Builder;

@Builder
public record ErrorDetail(
        String errorField,
        String errorMessage,
        Object inputValue
) {
    public static ErrorDetail of(String errorField, String errorMessage, Object inputValue) {
        return ErrorDetail.builder()
                .errorField(errorField)
                .errorMessage(errorMessage)
                .inputValue(inputValue)
                .build();
    }
}
