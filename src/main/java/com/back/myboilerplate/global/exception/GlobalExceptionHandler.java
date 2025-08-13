package com.back.myboilerplate.global.exception;


import com.back.myblog.global.common.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j //예외 발생시 로그 만들어주는 어노테이션
@RestControllerAdvice //spring에게 모든 컨트롤러에서 발생하는 예외를 가로채게 알려주는 어노
public class GlobalExceptionHandler {
    /**
     * 해당 부분 하단에 위 커스텀 예외 핸들러처럼 직접 제작하신 커스텀 예외 등록하시면 됩니다.
     * 어노테이션의 인자, 메서드 명, 파라미터 바꾸셔야 합니다.
     * */

    /*
     * CustomException 이라는 '예상된' 예외가 발생했을 때 호출되는 메서드
     */
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiResponse<Void>> handleCustomException(CustomException e) {
        // 1. 로그 남기기: 어떤 예외가 발생했는지, 그 원인(e)과 함께 기록한다. (디버깅에 매우 중요)
        log.info(e.getMessage(), e);

        // 2. 응답 본문(Body) 생성: ApiResponse의 fail 정적 메서드를 사용해 실패 응답 객체를 만든다.
        //    예외(e)가 가지고 있는 코드와 메시지를 그대로 사용한다.
        ApiResponse<Void> response = ApiResponse.fail(
                e.getCode(),
                e.getMessage()
        );

        // 3. 최종 응답(ResponseEntity) 생성 및 반환
        //    - status(): 예외(e)가 가지고 있는 HTTP 상태 코드를 실제 응답의 상태 코드로 설정한다. (e.g., 404 Not Found)
        //    - body(): 위에서 만든 실패 응답 객체를 본문에 담는다.
        return ResponseEntity.status(e.getHttpStatus()).body(response);
    }
    // 커스텀 예외는 다 이 위로 작성해야 함
    // 그외 모든 예외 처리 핸들러
    /*
     * 위에서 잡지 못한, '예상치 못한' 모든 예외를 처리하는 최후의 방어선
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception e) {
        // 1. (중요) 어떤 예외가 발생했는지 서버에만 심각한 에러 로그를 남긴다.
        log.error("Unhandled exception occurred", e); // 실제 운영에서는 error 레벨로 남기는 것이 좋다.

        // 2. 응답 본문 생성: 사용자에게는 구체적인 오류 대신, 서버 내부 오류라는 일반적인 메시지를 보낸다.
        ApiResponse<Void> response = ApiResponse.fail(
                "SERVER-500",
                "서버 내부 오류가 발생하였습니다."
        );

        // 3. 최종 응답 반환: HTTP 상태 코드는 500 (Internal Server Error)으로 고정한다.
        return ResponseEntity.status(500).body(response);
    }
}
