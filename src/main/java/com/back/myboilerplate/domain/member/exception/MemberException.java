package com.back.myboilerplate.domain.member.exception;

import com.back.myboilerplate.global.common.exception.BaseException;
import com.back.myboilerplate.global.common.exception.ExceptionInformation;

public class MemberException extends BaseException {
    public MemberException(ExceptionInformation exceptionInformation) {
        super(exceptionInformation);
    }
}

