package com.back.myboilerplate.global.common.exception;

import org.springframework.http.HttpStatus;

public interface ExceptionInformation {
    HttpStatus getHttpStatus();
    String getCode();
    String getMessage();
}
