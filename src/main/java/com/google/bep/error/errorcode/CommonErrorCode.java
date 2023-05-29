package com.google.bep.error.errorcode;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CommonErrorCode implements ErrorCode {  // 애플리케이션 전역 사용
    // 400
    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),

    // 500
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "내부 서버 에러");

    private final HttpStatus httpStatus;
    private final String message;
}
