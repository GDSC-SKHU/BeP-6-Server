package com.google.bep.error.handler;


import com.google.bep.error.dto.ResponseError;
import com.google.bep.error.errorcode.CommonErrorCode;
import com.google.bep.error.errorcode.CustomErrorCode;
import com.google.bep.error.errorcode.ErrorCode;
import com.google.bep.error.exception.RestApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    // 커스텀 예외 처리
    @ExceptionHandler(RestApiException.class)
    public ResponseEntity<Object> handleException(final RestApiException e) {
        final ErrorCode errorCode = e.getErrorCode();
        return handleExceptionInternal(errorCode);
    }

    // 적합하지 않거나(illegal) 적절하지 못한(inappropriate) 인자를 메소드에 넘겨줌
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgument(final IllegalArgumentException e) {
        log.warn("handleIllegalArgument", e);
        final ErrorCode errorCode = CommonErrorCode.INVALID_PARAMETER;
        return handleExceptionInternal(errorCode, e.getMessage());
    }

    // 로그인 실패
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Object> handleUnauthorized(final BadCredentialsException e) {
        log.warn("handleForbidden", e);
        final ErrorCode errorCode = CustomErrorCode.UNAUTHORIZED;
        return handleExceptionInternal(errorCode);
    }

    // 예상하지 못한 서버 에러
    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleAllException(final Exception ex) {
        log.warn("handleAllException", ex);
        final ErrorCode errorCode = CommonErrorCode.INTERNAL_SERVER_ERROR;
        return handleExceptionInternal(errorCode);
    }


    // Method Overloading

    // ErrorCode를 전달받아, 해당 에러코드에 맞는 HTTP 응답 코드와 함께 ErrorResponse 객체를 반환
    private ResponseEntity<Object> handleExceptionInternal(final ErrorCode errorCode) {
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(makeResponseError(errorCode));
    }

    // ErrorCode를 전달받아, 해당 에러코드에 맞는 HTTP 응답 코드와 함께 ErrorResponse 객체를 생성
    private ResponseError makeResponseError(final ErrorCode errorCode) {
        return ResponseError.builder()
                .status(errorCode.getHttpStatus().value())
                .code(errorCode.name())
                .message(errorCode.getMessage())
                .build();
    }

    // ErrorCode와 String 메시지를 전달받아, 해당 에러코드와 메시지에 맞는 HTTP 응답 코드와 함께 ErrorResponse 객체를 반환
    private ResponseEntity<Object> handleExceptionInternal(final ErrorCode errorCode, final String message) {
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(makeResponseError(errorCode, message));
    }

    // makeErrorResponse 메소드는 ErrorCode와 String 메시지를 전달받아, ErrorResponse 객체를 생성
    private ResponseError makeResponseError(final ErrorCode errorCode, final String message) {
        return ResponseError.builder()
                .status(errorCode.getHttpStatus().value())
                .code(errorCode.name())
                .message(message)
                .build();
    }
}
