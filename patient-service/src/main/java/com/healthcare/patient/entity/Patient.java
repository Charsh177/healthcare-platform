package com.healthcare.patient.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "patient_master", schema = "patient_schema")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Patient {
    
    @Id
    private UUID patientId = UUID.randomUUID();
    
    @Column(unique = true, nullable = false)
    private String uhid;
    
    @Column(nullable = false)
    private String firstName;
    
    @Column(nullable = false)
    private String lastName;
    
    @Column(nullable = false)
    private LocalDate dob;
    
    @Column
    private String gender;
    
    @Column(unique = true)
    private String mobile;
    
    @Column(unique = true)
    private String aadhaarNo;
    
    @Column
    private String address;
    
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
    
    @Column
    private LocalDateTime updatedAt;
    
    @Column
    private String createdBy;
    
    @Column
    private String updatedBy;
}
