package com.google.bep.mission.domain.repository;

import com.google.bep.mission.domain.model.UserComplete;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserCompleteRepository extends JpaRepository<UserComplete, Long> {
    @Query(value = "SELECT mission.id " +
            "FROM mission " +
            "LEFT JOIN user_complete ON mission.id = user_complete.mi_id AND user_complete.user_id = :id " +
            "LEFT JOIN user_mission ON mission.id = user_mission.mi_id AND user_mission.user_id = :id " +
            "WHERE user_complete.mi_id IS NULL AND user_mission.mi_id IS NULL " +
            "ORDER BY RAND() " +
            "LIMIT :cnt", nativeQuery = true)
    List<Long> getUserCompletesById(Long id, int cnt);
}
