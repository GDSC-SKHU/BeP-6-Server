package com.google.bep.mission.domain.model;

import com.google.bep.donation.domain.model.Donation;
import jakarta.persistence.*;
import lombok.Getter;


@Getter
@Entity
public class Mission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false)
    private String question;

    @Column(nullable = false)
    private String latitude;

    @Column(nullable = false)
    private String longitude;

    @Column(nullable = false)
    private int miPoint;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String imgUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "donation_id", nullable = false)
    Donation donation;
}
