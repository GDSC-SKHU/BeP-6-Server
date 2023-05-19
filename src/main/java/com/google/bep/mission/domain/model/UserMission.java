package com.google.bep.mission.domain.model;

import com.google.bep.account.domain.model.Account;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Entity
public class UserMission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mi_id", nullable = false)
    Mission mission;

}
