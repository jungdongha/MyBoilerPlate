package com.back.myboilerplate.domain.auth.exception;

import com.back.myboilerplate.global.common.exception.BaseException;
import com.back.myboilerplate.global.common.exception.ExceptionInformation;

public class AuthException extends BaseException {
    public AuthException(ExceptionInformation exceptionInformation) {
        super(exceptionInformation);
    }
}

