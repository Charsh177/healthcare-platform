package com.healthcare.patient.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "patient_visit", schema = "patient_schema")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientVisit {
    
    @Id
    private UUID visitId = UUID.randomUUID();
    
    @Column(nullable = false)
    private UUID patientId;
    
    @Column(nullable = false)
    private String visitType;
    
    @Column(nullable = false)
    private UUID doctorId;
    
    @Column(nullable = false)
    private LocalDateTime visitDate;
    
    @Column
    private String notes;
    
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}
