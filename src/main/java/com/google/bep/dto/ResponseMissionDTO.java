package com.google.bep.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ResponseMissionDTO {
    @Schema(description = "미션 id", example = "1")
    private Long id;

    @Schema(description = "미션 질문", example = "프랑스의 위치는 어디인가요?")
    private String question;

    @Schema(description = "카테고리", example = "LIFE BELOW WATER")
    private String category;

    @Schema(description = "위도", example = "37.715133")
    private String latitude;

    @Schema(description = "경도", example = "126.734086")
    private String longitude;

    @Schema(description = "미션 포인트", example = "500")
    private int miPoint;

}
