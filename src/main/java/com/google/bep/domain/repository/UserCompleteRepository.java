package com.google.bep.domain.repository;

import com.google.bep.domain.model.UserComplete;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserCompleteRepository extends JpaRepository<UserComplete, Long> {
    @Query(value="select mission.id from mission \n" +
                            "where mission.id \n" +
                            "not in (select user_complete.mi_id from user_complete where user_complete.user_id = :id \n" +
                            "UNION SELECT user_mission.mi_id FROM user_mission WHERE user_mission.user_id = :id) \n" +
                            "order by RAND() limit :cnt", nativeQuery=true)
    List<Long> getUserCompletesById(Long id, int cnt);
}
