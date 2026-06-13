package com.healthcare.auth.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "token_blacklist", schema = "auth_schema")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BlacklistedToken {

    @Id
    @Column(length = 1000)
    private String token;

    @Column(nullable = false)
    private LocalDateTime expiryTime;
}