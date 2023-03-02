package com.google.bep.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ResponseMissionDTO {

    private String question;

    private String category;

    private String latitude;

    private String longitude;

    private int miPoint;

}
