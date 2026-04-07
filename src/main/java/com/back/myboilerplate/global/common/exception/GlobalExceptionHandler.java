package com.back.myboilerplate.global.common.exception;

import com.back.myboilerplate.global.common.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ApiResponse<Void>> handleBaseException(BaseException e) {
        log.warn("BaseException: {}", e.getMessage());
        return ResponseEntity
                .status(e.getStatus())
                .body(ApiResponse.response(e.getStatus(), e.getCode(), e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<List<ErrorDetail>>> handleValidationException(MethodArgumentNotValidException e) {
        List<ErrorDetail> errors = e.getBindingResult().getFieldErrors().stream()
                .map(error -> ErrorDetail.of(error.getField(), error.getDefaultMessage(), error.getRejectedValue()))
                .collect(Collectors.toList());

        log.warn("ValidationException: {}", errors);
        HttpStatus status = HttpStatus.valueOf(e.getStatusCode().value());
        return ResponseEntity
                .status(status)
                .body(ApiResponse.response(status, "COMMON-001", "입력값이 유효하지 않습니다.", errors));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception e) {
        log.error("Internal Server Error", e);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.response(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON-000", "서버 내부 에러가 발생했습니다."));
    }
}
