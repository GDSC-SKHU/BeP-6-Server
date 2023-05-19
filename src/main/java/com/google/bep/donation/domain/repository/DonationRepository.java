package com.google.bep.donation.domain.repository;

import com.google.bep.donation.domain.model.Donation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DonationRepository extends JpaRepository<Donation, Long> {
    List<Donation> findAll();
    Optional<Donation> findByCategory (String category);
}