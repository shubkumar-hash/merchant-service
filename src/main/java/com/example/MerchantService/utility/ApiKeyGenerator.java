package com.example.MerchantService.utility;

import com.example.MerchantService.enums.Environment;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ApiKeyGenerator {

    public String generateApiKey(Environment env) {
        return "pk_" + env.name().toLowerCase() + "_" + UUID.randomUUID();
    }

    public String generateApiSecret(Environment env) {
        return "sk_" + env.name().toLowerCase() + "_" + UUID.randomUUID();
    }
}
