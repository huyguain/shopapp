package com.project.shopapp.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "social_accounts")
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SocialAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "provider_id", nullable = false, length = 20)
    private String providerId;

    @Column(name = "provider", nullable = false, length = 255)
    private String provider;

    @Column(name = "email", nullable = false, length = 150)
    private String email;

    @Column(name = "name", nullable = false, length = 100)
    private String name;
}
