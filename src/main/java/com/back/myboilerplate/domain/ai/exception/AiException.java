package com.back.myboilerplate.domain.ai.exception;

import com.back.myboilerplate.global.common.exception.BaseException;
import com.back.myboilerplate.global.common.exception.ExceptionInformation;

public class AiException extends BaseException {
    public AiException(ExceptionInformation exceptionInformation) {
        super(exceptionInformation);
    }
}

