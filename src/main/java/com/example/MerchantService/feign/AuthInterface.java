package com.example.MerchantService.feign;

import com.example.MerchantService.dto.AuthResponse;
import com.example.MerchantService.dto.LoginRequest;
import com.example.MerchantService.dto.authDto.RegisterRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("AUTH-SERVICE")
public interface AuthInterface {
    @PostMapping("api/auth/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request);

    @PostMapping("api/auth/refresh")
    public ResponseEntity<AuthResponse> refresh(@RequestParam String token);

    @PostMapping("api/auth/register")
    void register(RegisterRequest registerRequest);
}
