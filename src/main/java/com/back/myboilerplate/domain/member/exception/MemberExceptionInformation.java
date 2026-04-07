package com.back.myboilerplate.domain.member.exception;

import com.back.myboilerplate.global.common.exception.ExceptionInformation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum MemberExceptionInformation implements ExceptionInformation {

    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "MEM-001", "존재하지 않는 회원입니다."),
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "MEM-002", "이미 존재하는 이메일입니다."),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
