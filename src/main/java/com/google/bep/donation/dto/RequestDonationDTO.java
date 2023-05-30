package com.google.bep.donation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class RequestDonationDTO {
    @Schema(description = "기부할 포인트", example = "150")
    private int donationPoint;
}
