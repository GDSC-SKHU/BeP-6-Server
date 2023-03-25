package com.google.bep.error.exception;

import com.google.bep.error.errorcode.ErrorCode;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class RestApiException extends RuntimeException {    // 발생한 예외를 처리해줄 예외 클래스(Exception Class), 언체크 예외(런타임 예외)를 상속
    private final ErrorCode errorCode;
}
