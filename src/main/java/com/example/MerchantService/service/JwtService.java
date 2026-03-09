package com.example.MerchantService.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class JwtService {

    private final String SECRET = "super-secret-key-super-secret-key";

    public Claims extractAllClaims(String token) {
        try {

            return Jwts.parserBuilder()
                    .setSigningKey(SECRET.getBytes())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

        } catch (ExpiredJwtException e) {
            throw new RuntimeException("Token expired");

        } catch (Exception e) {
            throw new RuntimeException("Invalid token");
        }
    }

    public String extractEmail(String token) {
        return extractAllClaims(token).getSubject();
    }

    public String extractRole(String token) {
        return extractAllClaims(token).get("role", String.class);
    }

    public UUID extractMerchantId(String token) {
        return UUID.fromString(
                extractAllClaims(token).get("merchantId", String.class)
        );
    }

    public boolean isTokenValid(String token) {

        try {
            extractAllClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }

    }
}
