package com.google.bep.domain.model;

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
    private String category;

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
}
