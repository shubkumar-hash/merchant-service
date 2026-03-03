package com.example.MerchantService.controller;

import com.example.MerchantService.dto.ApiCredentialResponse;
import com.example.MerchantService.dto.MerchantResponse;
import com.example.MerchantService.enums.Environment;
import com.example.MerchantService.enums.MerchantStatus;
import com.example.MerchantService.dto.RegisterMerchantRequest;
import com.example.MerchantService.service.MerchantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/merchants")
@RequiredArgsConstructor
public class MerchantController {

    private final MerchantService merchantService;

    @PostMapping("/register")
    public ResponseEntity<MerchantResponse> register(
            @Valid @RequestBody RegisterMerchantRequest request) {
        return ResponseEntity.ok(merchantService.register(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MerchantResponse> get(@PathVariable UUID id) {
        return ResponseEntity.ok(merchantService.getMerchant(id));
    }

    @PostMapping("/{id}/api-keys")
    public ResponseEntity<ApiCredentialResponse> generateKeys(
            @PathVariable UUID id,
            @RequestParam Environment env) {
        return ResponseEntity.ok(merchantService.generateApiKeys(id, env));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> updateStatus(
            @PathVariable UUID id,
            @RequestParam MerchantStatus status) {
        merchantService.updateStatus(id, status);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        merchantService.deleteMerchant(id);
        return ResponseEntity.noContent().build();
    }
}
