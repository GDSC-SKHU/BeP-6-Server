package com.google.bep.donation.Service;

import com.google.bep.account.domain.model.Account;
import com.google.bep.donation.domain.model.Donation;
import com.google.bep.donation.domain.repository.DonationRepository;
import com.google.bep.donation.dto.DonationDTO;
import com.google.bep.error.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.google.bep.error.errorcode.CustomErrorCode.CATEGORY_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class DonationService {
    private final DonationRepository donationRepository;

    @Transactional
    public int donate(Account account, DonationDTO donationDTO) {
        if (account.getUserPoint() < donationDTO.getDonationPoint()) {
            throw new IllegalArgumentException("포인트가 부족합니다.");
        } else {
            account.updateUserPoint("-", donationDTO.getDonationPoint());
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
    public void updateDonationPoint (String category, int point) {
        Donation donation = donationRepository.findByCategory(category).orElseThrow(() -> new RestApiException(CATEGORY_NOT_FOUND));
        Donation updatedDonation = Donation.builder()
                .id(donation.getId())
                .category(donation.getCategory())
                .donationPoint(donation.getDonationPoint() + point)
                .build();
        donationRepository.save(updatedDonation);
    }
}
