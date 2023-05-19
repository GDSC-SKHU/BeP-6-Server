package com.google.bep.donation.dto;

import com.google.bep.donation.domain.model.Donation;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class DonationDTO {
    @Schema(description = "기부되는 카테고리", example = "LIFE BELOW WATER")
    private String category;

    @Schema(description = "유저가 기부한 포인트 또는 기부 포인트 총액", example = "100 or 32000")
    private int donationPoint;

    public static DonationDTO toDTO(Donation donation) {
        return DonationDTO.builder()
                .category(donation.getCategory())
                .donationPoint(donation.getDonationPoint())
                .build();
    }
}
