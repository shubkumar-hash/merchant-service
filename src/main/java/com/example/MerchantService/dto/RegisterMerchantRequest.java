package com.example.MerchantService.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterMerchantRequest {

    @NotBlank
    private String businessName;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String password;
}
