package com.back.myboilerplate.global.exception;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    DEV_NOT_FOUND(HttpStatus.NOT_FOUND, "aaa-404", "커스텀 예외 처리입니다");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

}
