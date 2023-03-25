package com.google.bep.error.errorcode;

import org.springframework.http.HttpStatus;

public interface ErrorCode {    // CommonErrorCode와 UserErrorCode의 공통 메소드로 추상화할 인터페이스
    String name();
    HttpStatus getHttpStatus();
    String getMessage();
}
