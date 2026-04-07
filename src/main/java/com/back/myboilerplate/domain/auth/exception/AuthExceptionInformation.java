package com.back.myboilerplate.domain.auth.exception;

import com.back.myboilerplate.global.common.exception.ExceptionInformation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum AuthExceptionInformation implements ExceptionInformation {

    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "AUTH-001", "이메일 또는 비밀번호가 올바르지 않습니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH-002", "유효하지 않은 토큰입니다."),
    TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "AUTH-003", "토큰을 찾을 수 없습니다."),
    TOKEN_MISMATCH(HttpStatus.UNAUTHORIZED, "AUTH-004", "토큰이 일치하지 않습니다. 다시 로그인해주세요."),
    ALREADY_LOGGED_OUT(HttpStatus.UNAUTHORIZED, "AUTH-005", "이미 로그아웃된 토큰입니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "AUTH-006", "접근 권한이 없습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
