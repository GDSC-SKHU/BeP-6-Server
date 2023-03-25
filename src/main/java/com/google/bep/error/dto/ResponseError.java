package com.google.bep.error.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;


@AllArgsConstructor
@Builder
@Getter
public class ResponseError {    // 에러 응답 클래스
    @Schema(description = "상태 코드 번호", example = "400")
    private final int status;

    @Schema(description = "에러 코드", example = "DUPLICATE_RESOURCE")
    private final String code;
    
    @Schema(description = "에러 코드 설명", example = "이미 가입된 이메일입니다.")
    private final String message;
}
