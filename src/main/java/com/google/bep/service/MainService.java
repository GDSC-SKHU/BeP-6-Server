package com.google.bep.service;

import com.google.bep.domain.model.Account;
import com.google.bep.domain.model.Mission;
import com.google.bep.domain.model.UserComplete;
import com.google.bep.domain.model.UserMission;
import com.google.bep.domain.repository.AccountRepository;
import com.google.bep.domain.repository.MissionRepository;
import com.google.bep.domain.repository.UserCompleteRepository;
import com.google.bep.domain.repository.UserMissionRepository;
import com.google.bep.dto.ResponseDetailDTO;
import com.google.bep.dto.ResponseMissionDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MainService {
    private final AccountRepository accountRepository;
    private final MissionRepository missionRepository;
    private final UserCompleteRepository userCompleteRepository;
    private final UserMissionRepository userMissionRepository;

    public List<ResponseMissionDTO> getMissions(String email) {
        Account account = getAccountByEmail(email);
        List<ResponseMissionDTO> missions = new ArrayList<>();
        int missionCnt = 3;
        int unassignedCnt = (int) (missionCnt - userMissionRepository.countByAccount_Id(account.getId()));

        // 할당받은 미션 개수가 3개가 아닐 경우 추가로 할당
        if(unassignedCnt != 0) {
            List<Long> ids = userCompleteRepository.getUserCompletesById(account.getId(), unassignedCnt);
            if(!ids.isEmpty()) saveUserMissions(account, ids);
            else missionCnt -= unassignedCnt;
        }

        /* Eager는 연관관계까지 즉시 함께 조회하는 반면에, Lazy는 연관관계의 데이터를 실질적으로 요구할 때 조회를 함.
            그래서 getMission을 써도 Mission의 실질적인 데이터는 가져오지 않은 상태 */
        // DTO에 실질적인 데이터 값을 넣어서 DTO를 list에 넣어서 해결!
        List<UserMission> userMissions = getUserMissions(account);
        for(UserMission userMission : userMissions) {    // 미션DTO 채워서 반환
            Mission mission = userMission.getMission();
            ResponseMissionDTO missionDTO = mission.toDTO();
            missions.add(missionDTO);
        }
        return missions;
    }

    // UserMission에 데이터 저장
    private void saveUserMissions(Account account, List<Long> missionIds) {
        for (Long id : missionIds) {
            Optional<Mission> mission = missionRepository.findById(id);
            mission.ifPresent(m -> {
                userMissionRepository.save(UserMission.builder()
                        .account(account)
                        .mission(m)
                        .build());
            });
        }
    }

    private List<UserMission> getUserMissions(Account account) {
        return userMissionRepository.findByAccount_Id(account.getId());
    }

    private Account getAccountByEmail(String email) {
        return accountRepository.findByEmail(email).orElse(null);
    }

    public ResponseDetailDTO completeMission(String email, Long missionId) {
        Account account = getAccountByEmail(email);
        Mission mission = missionRepository.findById(missionId).orElse(null);
        ResponseDetailDTO detailDTO = mission.toDetailDTO();        // 해당 미션id로 detailDTO 채우기

        // 해당 미션의 포인트를 로그인한 유저의 유저포인트에 적립
        account.updatePoint(mission.getMiPoint());
        detailDTO.setUserPoint(account.getUserPoint());

        // user_complete에 로그인 한 유저가 완료된 미션 저장
        userCompleteRepository.save(UserComplete.builder()
                    .account(account)
                    .mission(mission)
                    .build());

        // user_mission에 있는 로그인 한 유저id의 미션id는 삭제
        userMissionRepository.deleteUserMission(account.getId(), missionId);
        return detailDTO;
    }
}