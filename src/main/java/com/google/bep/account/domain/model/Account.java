package com.google.bep.account.domain.model;


import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private int userPoint;

    @Column(nullable = false)
    private String provider;

    @Enumerated(EnumType.STRING)
    private Role role; // 사용자 권한

    public void updateUserPoint(String op, int point) {
        if(op.equals("+")) this.userPoint += point;
        else if(op.equals("-") && point > 0) this.userPoint -= point;
    }
}
