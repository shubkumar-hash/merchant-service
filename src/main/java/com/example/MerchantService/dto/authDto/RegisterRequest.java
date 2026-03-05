package com.example.MerchantService.dto.authDto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Builder
@Data
public class RegisterRequest {
    private UUID merchantId;
    private String email;
    private String password;
}
