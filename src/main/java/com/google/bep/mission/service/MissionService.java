package com.google.bep.mission.service;

import com.google.bep.account.domain.model.Account;
import com.google.bep.account.service.AccountService;
import com.google.bep.error.exception.RestApiException;
import com.google.bep.mission.domain.model.Mission;
import com.google.bep.mission.domain.model.UserComplete;
import com.google.bep.mission.domain.model.UserMission;
import com.google.bep.mission.domain.repository.MissionRepository;
import com.google.bep.mission.domain.repository.UserCompleteRepository;
import com.google.bep.mission.domain.repository.UserMissionRepository;
import com.google.bep.mission.dto.ResponseDetailDTO;
import com.google.bep.mission.dto.ResponseMissionDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.google.bep.error.errorcode.CommonErrorCode.INVALID_PARAMETER;
import static com.google.bep.error.errorcode.CustomErrorCode.UNASSIGNED_MISSION;

@Service
@RequiredArgsConstructor
public class MissionService {
    private final MissionRepository missionRepository;
    private final UserCompleteRepository userCompleteRepository;
    private final UserMissionRepository userMissionRepository;

    private final AccountService accountService;

    @Transactional
    public List<ResponseMissionDTO> getMissions(Account account) {
        List<ResponseMissionDTO> missions = new ArrayList<>();
        int unassignedCnt = (int) (3 - userMissionRepository.countByAccount_Id(account.getId()));

        // 할당받은 미션 개수가 3개가 아닐 경우 추가로 할당
        if (unassignedCnt != 0) {
            List<Long> ids = userCompleteRepository.getUserCompletesById(account.getId(), unassignedCnt);
            if (!ids.isEmpty()) saveUserMissions(account, ids);
        }

        /* Eager는 되도록 쓰지 않는 것 권장!
            Eager는 연관관계까지 즉시 함께 조회하는 반면에, Lazy는 연관관계의 데이터를 실질적으로 요구할 때 조회를 함.
            그래서 getMission을 써도 Mission의 실질적인 데이터는 가져오지 않은 상태 */
        // DTO에 실질적인 데이터 값을 넣어서 DTO를 list에 넣어서 해결!
        List<UserMission> userMissions = getUserMissions(account);

        for (UserMission userMission : userMissions) {    // 미션DTO 채워서 반환
            Mission mission = userMission.getMission();
            ResponseMissionDTO missionDTO = ResponseMissionDTO.toDTO(mission);
            missions.add(missionDTO);
        }
        return missions;
    }

    @Transactional
    // UserMission에 데이터 저장
    public void saveUserMissions(Account account, List<Long> missionIds) {
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

    @Transactional(readOnly = true)
    public List<UserMission> getUserMissions(Account account) {
        return userMissionRepository.findByAccount_Id(account.getId());
    }


    @Transactional
    public ResponseDetailDTO completeMission(Account account, Long missionId) {
        Mission mission = findMissionById(missionId);
        checkUserMissionExists(account.getId(), missionId);

        accountService.updateUserPoint(account,"+" ,mission.getMiPoint());
        saveUserComplete(account, mission);
        deleteUserMission(account.getId(), missionId);

        ResponseDetailDTO detailDTO = ResponseDetailDTO.toDetailDTO(mission);
        detailDTO.setUserPoint(account.getUserPoint());

        return detailDTO;
    }

    public Mission findMissionById(Long missionId) {
        return missionRepository.findById(missionId)
                .orElseThrow(() -> new RestApiException(INVALID_PARAMETER));
    }

    public void checkUserMissionExists(Long userId, Long missionId) {
        userMissionRepository.findUserMission(userId, missionId)
                .orElseThrow(() -> new RestApiException(UNASSIGNED_MISSION));
    }

    public void saveUserComplete(Account account, Mission mission) {
        userCompleteRepository.save(UserComplete.builder()
                .account(account)
                .mission(mission)
                .build());
    }

    public void deleteUserMission(Long userId, Long missionId) {
        userMissionRepository.deleteUserMission(userId, missionId);
    }
}