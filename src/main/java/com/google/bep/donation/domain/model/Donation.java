package com.google.bep.donation.domain.model;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
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

    public void updateDonationPoint(int point) {
        this.donationPoint += point;
    }
}
