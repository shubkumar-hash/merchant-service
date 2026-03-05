package com.example.MerchantService.dto;

import com.example.MerchantService.enums.MerchantStatus;
import com.example.MerchantService.enums.PlanType;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class MerchantResponse {

    private UUID merchantId;
    private String businessName;
    private String email;
    private MerchantStatus status;
    private PlanType planType;
}