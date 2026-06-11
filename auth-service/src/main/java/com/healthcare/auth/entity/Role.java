package com.healthcare.auth.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Entity
@Table(name = "roles", schema = "auth_schema")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role {
    
    @Id
    private UUID roleId = UUID.randomUUID();
    
    @Column(unique = true, nullable = false)
    private String roleName;
    
    @Column
    private String description;
}
