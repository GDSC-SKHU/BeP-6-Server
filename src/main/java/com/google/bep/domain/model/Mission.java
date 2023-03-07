package com.google.bep.domain.model;

import com.google.bep.dto.ResponseDetailDTO;
import com.google.bep.dto.ResponseMissionDTO;
import jakarta.persistence.*;
import lombok.*;



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


    public ResponseMissionDTO toDTO() {
        return ResponseMissionDTO.builder()
                .id(this.id)
                .question(this.question)
                .category(this.category)
                .longitude(this.longitude)
                .latitude(this.latitude)
                .miPoint(this.miPoint).build();
    }

    public ResponseDetailDTO toDetailDTO() {
        return ResponseDetailDTO.builder()
                .question(this.question)
                .category(this.category)
                .content(this.content)
                .imgUrl(this.imgUrl).build();
    }
}
