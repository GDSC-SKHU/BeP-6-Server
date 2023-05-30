package com.google.bep.donation.Service;

import com.google.bep.account.domain.model.Account;
import com.google.bep.account.service.AccountService;
import com.google.bep.donation.domain.model.Donation;
import com.google.bep.donation.domain.repository.DonationRepository;
import com.google.bep.donation.dto.RequestDonationDTO;
import com.google.bep.error.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.google.bep.error.errorcode.CommonErrorCode.INVALID_PARAMETER;
import static com.google.bep.error.errorcode.CustomErrorCode.CATEGORY_NOT_FOUND;
import static com.google.bep.error.errorcode.CustomErrorCode.INVALID_DONATION_POINT;

@Service
@RequiredArgsConstructor
public class DonationService {
    private final DonationRepository donationRepository;

    private final AccountService accountService;

    @Transactional
    public int donate(Account account, RequestDonationDTO donationDTO, Long donationId) {
        if (account.getUserPoint() < donationDTO.getDonationPoint()) {
            throw new RestApiException(INVALID_DONATION_POINT);
        } else if (0 < donationDTO.getDonationPoint()) {
            accountService.updateUserPoint(account, "-", donationDTO.getDonationPoint());

            Donation donation = findById(donationId);
            donation.updateDonationPoint(donationDTO.getDonationPoint());
            donationRepository.save(donation);

        } else throw new RestApiException(INVALID_PARAMETER);
        return account.getUserPoint();
    }

    @Transactional(readOnly = true)
    public List<Donation> findAll() {
        List<Donation> donations = donationRepository.findAll(Sort.by(Sort.Direction.DESC, "donationPoint"));
        return donations;
    }

    @Transactional(readOnly = true)
    public Donation findById(Long id) {
        return donationRepository.findById(id).orElseThrow(() -> new RestApiException(CATEGORY_NOT_FOUND));
    }
}
