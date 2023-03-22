package com.google.bep.domain.model;

import com.google.bep.dto.DonationDTO;
import jakarta.persistence.*;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Donation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private int donationPoint;

    public DonationDTO toDTO() {
        return DonationDTO.builder()
                .category(this.category)
                .donationPoint(this.donationPoint)
                    .build();
    }

    public void update(int point) {
        this.donationPoint += point;
    }
}
