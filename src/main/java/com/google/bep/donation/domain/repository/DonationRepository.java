package com.google.bep.donation.domain.repository;

import com.google.bep.donation.domain.model.Donation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DonationRepository extends JpaRepository<Donation, Long> {
    List<Donation> findAll();
}
