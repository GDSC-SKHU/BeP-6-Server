package com.google.bep.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ResponseDetailDTO {
    @Schema(description = "미션 질문", example = "프랑스의 위치는 어디인가요?")
    private String question;

    @Schema(description = "카테고리", example = "LIFE BELOW WATER")
    private String category;

    @Schema(description = "미션 상세", example = "프랑스 공화국, 약칭 프랑스는 서유럽의 본토와 남아메리카의 프랑스령 기아나를 비롯해 여러 대륙에 걸친 해외 지역으로 이루어진 국가이다.")
    private String content;

    @Schema(description = "미션 웹 이미지", example = "https://c.pxhere.com/photos/fd/16/water_ocean_sea_texture_nature_blue_summer_pattern-673024.jpg!d")
    private String imgUrl;
}
