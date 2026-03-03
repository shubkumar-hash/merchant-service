package com.example.MerchantService.model;

import com.example.MerchantService.enums.Environment;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "merchant_api_credentials")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MerchantApiCredential {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "merchant_id", nullable = false)
    private Merchant merchant;

    @Column(unique = true, nullable = false)
    private String apiKey;

    @Column(nullable = false)
    private String apiSecretHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Environment environment;

    @Column(nullable = false)
    private boolean active;

    private LocalDateTime createdAt;
    private LocalDateTime revokedAt;

    @PrePersist
    void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
