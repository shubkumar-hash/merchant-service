package com.example.MerchantService.service;

import com.example.MerchantService.dto.*;
import com.example.MerchantService.dto.authDto.RegisterRequest;
import com.example.MerchantService.enums.Environment;
import com.example.MerchantService.enums.MerchantStatus;
import com.example.MerchantService.enums.PlanType;
import com.example.MerchantService.exception.DuplicateResourceException;
import com.example.MerchantService.exception.InvalidOperationException;
import com.example.MerchantService.exception.ResourceNotFoundException;
import com.example.MerchantService.feign.AuthInterface;
import com.example.MerchantService.model.Merchant;
import com.example.MerchantService.model.MerchantApiCredential;
import com.example.MerchantService.repository.MerchantApiCredentialRepository;
import com.example.MerchantService.repository.MerchantRepository;
import com.example.MerchantService.utility.ApiKeyGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.spec.PSource;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class MerchantService {

    private final MerchantRepository merchantRepository;
    private final MerchantApiCredentialRepository credentialRepository;
    private final ApiKeyGenerator apiKeyGenerator;
    private final PasswordEncoder passwordEncoder;
    private final AuthInterface authInterface;

    public MerchantResponse register(RegisterMerchantRequest request) {

        merchantRepository.findByEmailAndDeletedFalse(request.getEmail())
                .ifPresent(m -> {
                    throw new DuplicateResourceException("Email already exists");
                });

        Merchant merchant = Merchant.builder()
                .businessName(request.getBusinessName())
                .email(request.getEmail())
                .status(MerchantStatus.PENDING)
                .planType(PlanType.FREE)
                .deleted(false)
                .build();

        merchantRepository.save(merchant);

        RegisterRequest registerRequest = RegisterRequest.builder()
                .merchantId(merchant.getMerchantId())
                .email(merchant.getEmail())
                .password(request.getPassword())
                .build();

        authInterface.register(registerRequest);

        return mapToResponse(merchant);
    }

    public MerchantResponse getMerchant(UUID id) {
        Merchant merchant = getActiveMerchant(id);
        return mapToResponse(merchant);
    }

    public ApiCredentialResponse generateApiKeys(UUID merchantId, Environment env) {

        Merchant merchant = getActiveMerchant(merchantId);

        if (merchant.getStatus() != MerchantStatus.ACTIVE) {
            throw new InvalidOperationException("Merchant must be ACTIVE to generate keys");
        }

        String apiKey = apiKeyGenerator.generateApiKey(env);
        String rawSecret = apiKeyGenerator.generateApiSecret(env);
        String hash = passwordEncoder.encode(rawSecret);

        MerchantApiCredential credential = MerchantApiCredential.builder()
                .merchant(merchant)
                .apiKey(apiKey)
                .apiSecretHash(hash)
                .environment(env)
                .active(true)
                .build();

        credentialRepository.save(credential);

        return ApiCredentialResponse.builder()
                .apiKey(apiKey)
                .apiSecret(rawSecret)
                .environment(env)
                .build();
    }

    public void updateStatus(UUID id, MerchantStatus status) {
        Merchant merchant = getActiveMerchant(id);
        merchant.setStatus(status);
        merchantRepository.save(merchant);
    }

    public void deleteMerchant(UUID id) {
        Merchant merchant = getActiveMerchant(id);
        merchant.setDeleted(true);
        merchant.setStatus(MerchantStatus.TERMINATED);
        merchantRepository.save(merchant);
    }

    private Merchant getActiveMerchant(UUID id) {
        return merchantRepository.findById(id)
                .filter(m -> !m.isDeleted())
                .orElseThrow(() -> new ResourceNotFoundException("Merchant not found"));
    }

    private MerchantResponse mapToResponse(Merchant merchant) {
        return MerchantResponse.builder()
                .merchantId(merchant.getMerchantId())
                .businessName(merchant.getBusinessName())
                .email(merchant.getEmail())
                .status(merchant.getStatus())
                .planType(merchant.getPlanType())
                .build();
    }

    public AuthResponse login(LoginRequest request) {
        return authInterface.login(request).getBody();
    }

    public AuthResponse refreshToken(String token) {
        return authInterface.refresh(token).getBody();
    }
}