package com.healthcare.claims.repository;

import com.healthcare.claims.entity.Claim;
import com.healthcare.claims.enums.ClaimStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClaimRepository extends JpaRepository<Claim, Long> {

    Optional<Claim> findByClaimNumber(String claimNumber);

    List<Claim> findByPatientId(Long patientId);

    List<Claim> findByStatus(ClaimStatus status);

    List<Claim> findByInsuranceCompany(String insuranceCompany);

    Optional<Claim> findByPolicyNumber(String policyNumber);

    List<Claim> findByDocumentsVerified(Boolean documentsVerified);
}