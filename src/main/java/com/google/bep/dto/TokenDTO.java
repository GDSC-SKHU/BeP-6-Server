package com.google.bep.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class TokenDTO {
    @Schema(description = "토큰 타입", example = "bearer")
    private String grantType;
    @Schema(description = "액세스 토큰")
    private String accessToken;
}
