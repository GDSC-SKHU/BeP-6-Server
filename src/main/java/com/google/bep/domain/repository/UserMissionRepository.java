package com.google.bep.domain.repository;

import com.google.bep.domain.model.UserMission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserMissionRepository extends JpaRepository<UserMission, Long> {
    List<UserMission> findByAccount_Id(Long id);
    Long countByAccount_Id(Long id);
}
