package com.google.bep.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ErrorResult {
    @Schema(description = "상태코드", example = "403")
    private Integer status;

    @Schema(description = "에러 요약", example = "Forbidden")
    private String code;

    @Schema(description = "에러 설명", example = "권한이 없습니다.")
    private String message;
}
