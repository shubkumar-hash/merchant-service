package com.example.MerchantService.dto;

import com.example.MerchantService.enums.Environment;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApiCredentialResponse {

    private String apiKey;
    private String apiSecret; // return only once
    private Environment environment;
}
