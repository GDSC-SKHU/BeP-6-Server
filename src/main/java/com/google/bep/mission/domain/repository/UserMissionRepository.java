package com.google.bep.mission.domain.repository;

import com.google.bep.mission.domain.model.UserMission;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserMissionRepository extends JpaRepository<UserMission, Long> {
    List<UserMission> findByAccount_Id(Long id);
    Long countByAccount_Id(Long id);
    Optional<UserMission> findById(Long id);

    // repository단에서 DB를 갱신/삭제할 경우 필요한 어노테이션
    @Modifying
    @Transactional
    @Query("DELETE FROM UserMission um WHERE um.account.id = :uId AND um.mission.id = :miId")
    void deleteUserMission(Long uId, Long miId);

    @Query("SELECT um.mission FROM UserMission um WHERE um.account.id = :uId AND um.mission.id = :miId")
    Optional<Long> findUserMission(Long uId, Long miId);
}
