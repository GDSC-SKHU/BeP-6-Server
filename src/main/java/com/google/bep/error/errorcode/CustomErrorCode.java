package com.google.bep.error.errorcode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CustomErrorCode implements ErrorCode {    // 특정 도메인 사용
    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 카테고리입니다."),
    DUPLICATE_RESOURCE(HttpStatus.CONFLICT, "이미 가입된 이메일입니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "아이디 또는 비밀번호를 잘못 입력했습니다."),

    UNASSIGNED_MISSION(HttpStatus.NOT_FOUND, "유저가 할당받은 미션이 아닙니다.")
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
