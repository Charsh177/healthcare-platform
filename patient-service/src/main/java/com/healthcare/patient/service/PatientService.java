package com.healthcare.patient.service;

import com.healthcare.patient.dto.PatientRequest;
import com.healthcare.patient.dto.PatientResponse;
import com.healthcare.patient.entity.Patient;
import com.healthcare.patient.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PatientService {

    private final PatientRepository patientRepository;

    public PatientResponse createPatient(PatientRequest request) {
        String uhid = generateUHID();
        
        Patient patient = Patient.builder()
                .uhid(uhid)
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .dob(request.getDob())
                .gender(request.getGender())
                .mobile(request.getMobile())
                .aadhaarNo(request.getAadhaarNo())
                .address(request.getAddress())
                .build();

        Patient savedPatient = patientRepository.save(patient);
        return mapToResponse(savedPatient);
    }

    public PatientResponse getPatientById(UUID patientId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        return mapToResponse(patient);
    }

    public PatientResponse getPatientByUHID(String uhid) {
        Patient patient = patientRepository.findByUhid(uhid)
                .orElseThrow(() -> new RuntimeException("Patient with UHID not found"));
        return mapToResponse(patient);
    }

    public PatientResponse updatePatient(UUID patientId, PatientRequest request) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        patient.setFirstName(request.getFirstName());
        patient.setLastName(request.getLastName());
        patient.setDob(request.getDob());
        patient.setGender(request.getGender());
        patient.setMobile(request.getMobile());
        patient.setAddress(request.getAddress());

        Patient updatedPatient = patientRepository.save(patient);
        return mapToResponse(updatedPatient);
    }

    public List<PatientResponse> searchPatients(String query) {
        List<Patient> patients = patientRepository.searchByName(query);
        return patients.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    private PatientResponse mapToResponse(Patient patient) {
        return PatientResponse.builder()
                .patientId(patient.getPatientId())
                .uhid(patient.getUhid())
                .firstName(patient.getFirstName())
                .lastName(patient.getLastName())
                .dob(patient.getDob())
                .gender(patient.getGender())
                .mobile(patient.getMobile())
                .aadhaarNo(patient.getAadhaarNo())
                .address(patient.getAddress())
                .createdAt(patient.getCreatedAt())
                .build();
    }

    private String generateUHID() {
        return "UH-" + System.currentTimeMillis() + "-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
    }
}
