package com.healthcare.patient.repository;

import com.healthcare.patient.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PatientRepository extends JpaRepository<Patient, UUID> {
    Optional<Patient> findByUhid(String uhid);
    Optional<Patient> findByMobile(String mobile);
    
    @Query("SELECT p FROM Patient p WHERE LOWER(CONCAT(p.firstName, ' ', p.lastName)) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Patient> searchByName(String name);
    
    @Query("SELECT p FROM Patient p WHERE p.aadhaarNo = :aadhaarNo")
    Optional<Patient> findByAadhaarNo(String aadhaarNo);
}
