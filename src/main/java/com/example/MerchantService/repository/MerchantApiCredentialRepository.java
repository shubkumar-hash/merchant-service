package com.example.MerchantService.repository;

import com.example.MerchantService.model.MerchantApiCredential;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface MerchantApiCredentialRepository
        extends JpaRepository<MerchantApiCredential, UUID> {

    Optional<MerchantApiCredential> findByApiKeyAndActiveTrue(String apiKey);
}
