package com.google.bep.service;

import com.google.bep.domain.model.*;
import com.google.bep.domain.repository.*;
import com.google.bep.dto.DonationDTO;
import com.google.bep.dto.ResponseDetailDTO;
import com.google.bep.dto.ResponseMissionDTO;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final DonationRepository donationRepository;

    @Transactional
    public List<ResponseMissionDTO> getMissions(String email) {
        Account account = getAccountByEmail(email);
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

    @Transactional
    public List<UserMission> getUserMissions(Account account) {
        return userMissionRepository.findByAccount_Id(account.getId());
    }

    @Transactional
    public Account getAccountByEmail(String email) {
        return accountRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("존재하지 않은 사용자"));
    }

    @Transactional
    public ResponseDetailDTO completeMission(String email, Long missionId) {
        Account account = getAccountByEmail(email);
        Mission mission = missionRepository.findById(missionId).orElseThrow();
        ResponseDetailDTO detailDTO = ResponseDetailDTO.toDetailDTO(mission);        // 해당 미션id로 detailDTO 채우기

        // 해당 미션의 포인트를 로그인한 유저의 유저포인트에 적립
        updateUserPoint(account, "+", mission.getMiPoint());
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

    @Transactional
    public int donate(String email, DonationDTO donationDTO) throws Exception {
        Account account = getAccountByEmail(email);
        if (account.getUserPoint() < donationDTO.getDonationPoint()) {
            throw new Exception("포인트가 부족합니다.");
        } else {
            updateUserPoint(account, "-", donationDTO.getDonationPoint());
            updateDonationPoint(donationDTO.getCategory(), donationDTO.getDonationPoint());
        }
        return account.getUserPoint();
    }

    @Transactional(readOnly = true)
    public List<DonationDTO> getDonations() {
        List<Donation> donations = donationRepository.findAll(Sort.by(Sort.Direction.DESC, "donationPoint"));
        List<DonationDTO> donationDTOList = new ArrayList<>();

        for (Donation donation : donations) {    // 미션DTO 채워서 반환
            DonationDTO donationDTO = DonationDTO.toDTO(donation);
            donationDTOList.add(donationDTO);
        }
        return donationDTOList;
    }

    @Transactional
    public void updateUserPoint(Account account, String op, int point) {
        Account updatedAccount = Account.builder()
                .id(account.getId())
                .email(account.getEmail())
                .name(account.getName())
                .password(account.getPassword())
                .roles(account.getRoles())
                .userPoint(op.equals("+") ? account.getUserPoint() + point : account.getUserPoint() - point)
                .provider(account.getProvider())
                .build();
        accountRepository.save(updatedAccount);
    }

    @Transactional
    public void updateDonationPoint (String category, int point) {
        Donation donation = donationRepository.findByCategory(category).orElseThrow(() -> new EntityNotFoundException("Category not found"));
        Donation updatedDonation = Donation.builder()
                                    .id(donation.getId())
                                    .category(donation.getCategory())
                                    .donationPoint(donation.getDonationPoint() + point)
                                    .build();
        donationRepository.save(updatedDonation);
    }
}