package com.healthcare.claims.controller;

import com.healthcare.claims.dto.ClaimRequest;
import com.healthcare.claims.dto.ClaimStatusUpdateRequest;
import com.healthcare.claims.entity.Claim;
import com.healthcare.claims.service.ClaimService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/claims")
public class ClaimController {

    private final ClaimService service;

    public ClaimController(ClaimService service) {
        this.service = service;
    }

    @GetMapping("/test")
    public String test() {
        return "Working";
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public Claim createClaim(@Valid @RequestBody ClaimRequest request) {
        return service.createClaim(request);
    }

    @GetMapping("/getAll")
    public List<Claim> getAllClaims() {
        return service.getAllClaims();
    }

    @GetMapping("/{id}")
    public Claim getClaim(@PathVariable Long id) {
        return service.getClaim(id);
    }

    @PutMapping("/{id}/status")
    public Claim updateStatus(@PathVariable Long id, @Valid @RequestBody ClaimStatusUpdateRequest request) {
        return service.updateStatus(id, request.getStatus());
    }
}