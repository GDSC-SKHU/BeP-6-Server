package com.google.bep.domain.repository;

import com.google.bep.domain.model.Mission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MissionRepository extends JpaRepository<Mission, Long> {
    Optional<Mission> findById(Long id);
}
