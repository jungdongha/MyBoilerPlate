package com.back.myboilerplate.global.common;


import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ApiResponse<T> {
    private final boolean success;
    private final String code;
    private final String msg;
    private final T content;

    private final static String SUCCESS_code = "200";

    //성공 응답 생성 메서드
    public static <T> ApiResponse<T> success(T content) {
        return ApiResponse.<T>builder()
                .success(true)
                .code(SUCCESS_code)
                .msg("success")
                .content(content)
                .build();
    }

    public static <T> ApiResponse<T> success(String msg, T content) {
        return ApiResponse.<T>builder()
                .success(true)
                .code(SUCCESS_code)
                .msg(msg)
                .content(content)
                .build();
    }
    // 실패 응답 생성 메서드
    public static <T> ApiResponse<T> fail(String code, String msg) {
        return ApiResponse.<T>builder()
                .success(false)
                .code(code)
                .msg(msg)
                .build();

    }
}
