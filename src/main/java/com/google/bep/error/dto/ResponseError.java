package com.google.bep.error.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;


@AllArgsConstructor
@Builder
@Getter
public class ResponseError {    // 에러 응답 클래스
    @Schema(description = "상태 코드 번호", example = "100")
    private final int status;

    @Schema(description = "에러 코드", example = "에러 코드")
    private final String code;
    
    @Schema(description = "에러 코드 설명", example = "에러 코드에 대한 설명")
    private final String message;
}
