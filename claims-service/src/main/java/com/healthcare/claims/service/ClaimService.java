package com.healthcare.claims.service;

import com.healthcare.claims.dto.ClaimRequest;
import com.healthcare.claims.entity.Claim;
import com.healthcare.claims.enums.ClaimStatus;
import com.healthcare.claims.repository.ClaimRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClaimService {

    private final ClaimRepository repository;

    public Claim createClaim(ClaimRequest request) {

        validateClaimRequest(request);

        // TODO:
        // Call Patient Service and verify patient exists
        // patientClient.getPatient(request.getPatientId());

        Claim claim = new Claim();

        claim.setClaimNumber(generateClaimNumber());
        claim.setPatientId(request.getPatientId());
        claim.setUhid(request.getUhid());

        claim.setInsuranceCompany(request.getInsuranceCompany());
        claim.setTpaName(request.getTpaName());

        claim.setPolicyNumber(request.getPolicyNumber());
        claim.setMemberId(request.getMemberId());

        claim.setClaimAmount(request.getClaimAmount());

        claim.setStatus(ClaimStatus.CREATED);

        claim.setDocumentsVerified(false);

        claim.setAdmissionDate(request.getAdmissionDate());
        claim.setDischargeDate(request.getDischargeDate());

        claim.setCreatedAt(LocalDateTime.now());
        claim.setUpdatedAt(LocalDateTime.now());

        Claim savedClaim = repository.save(claim);

        log.info("Claim created successfully. Claim Number : {}",
                savedClaim.getClaimNumber());

        return savedClaim;
    }

    public List<Claim> getAllClaims() {
        return repository.findAll();
    }

    public Claim getClaim(Long id) {

        return repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Claim not found with id : " + id));
    }

    public Claim updateStatus(Long id,
                              ClaimStatus status) {

        Claim claim = getClaim(id);

        validateStatusTransition(
                claim.getStatus(),
                status);

        claim.setStatus(status);
        claim.setUpdatedAt(LocalDateTime.now());

        log.info("Claim status updated. ClaimId={} Status={}",
                id,
                status);

        return repository.save(claim);
    }

    private String generateClaimNumber() {

        return "CLM-"
                + UUID.randomUUID()
                .toString()
                .substring(0, 8)
                .toUpperCase();
    }

    private void validateClaimRequest(
            ClaimRequest request) {

        if (request.getPatientId() == null) {
            throw new IllegalArgumentException(
                    "Patient Id is mandatory");
        }

        if (request.getClaimAmount() == null
                || request.getClaimAmount() <= 0) {

            throw new IllegalArgumentException(
                    "Invalid claim amount");
        }

        if (request.getInsuranceCompany() == null
                || request.getInsuranceCompany().isBlank()) {

            throw new IllegalArgumentException(
                    "Insurance Company is mandatory");
        }
    }

    private void validateStatusTransition(
            ClaimStatus currentStatus,
            ClaimStatus newStatus) {

        if (currentStatus == ClaimStatus.SETTLED ||
                currentStatus == ClaimStatus.REJECTED) {

            throw new IllegalStateException(
                    "Claim already completed");
        }
    }
}