package com.healthcare.auth.service;

import com.healthcare.auth.dto.LoginRequest;
import com.healthcare.auth.dto.TokenResponse;
import com.healthcare.auth.dto.UserRegisterRequest;
import com.healthcare.auth.entity.User;
import com.healthcare.auth.entity.BlacklistedToken;
import com.healthcare.auth.entity.Role;
import com.healthcare.auth.repository.BlacklistedTokenRepository;
import com.healthcare.auth.repository.RoleRepository;
import com.healthcare.auth.repository.UserRepository;
import com.healthcare.auth.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BlacklistedTokenRepository blacklistedTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public TokenResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new RuntimeException("Invalid credentials");
        }

        if (!user.getActive()) {
            throw new RuntimeException("User account is inactive");
        }

        String accessToken = jwtTokenProvider.generateAccessToken(user.getUsername(), user.getUserId().toString());
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getUsername(), user.getUserId().toString());

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .expiresIn(900L)
                .tokenType("Bearer")
                .build();
    }

    public User register(UserRegisterRequest request) {

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        Role role = null;
        if (request.getRoleId() != null && !request.getRoleId().isBlank()) {
            log.info("Attempting to find role with ID: '{}'", request.getRoleId());
            role = roleRepository.findById(UUID.fromString(request.getRoleId()))
                .orElseThrow(() -> new RuntimeException("Role not found for ID: " + request.getRoleId()));
        } else {
            role = roleRepository.findByRoleName("USER")
                .orElseThrow(() -> new RuntimeException("Default role 'USER' not configured"));
        }

        User user = User.builder()
                .username(request.getUsername())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .role(role)  // Assigns either the specified role or the default fallback
                .active(true)
                .build();

        log.info("Register request: {}", request);
        User savedUser = userRepository.save(user);
        log.info("Saved user: {}", savedUser);

        return savedUser;
    }

    public TokenResponse refreshToken(String refreshToken) {
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new RuntimeException("Invalid refresh token");
        }

        String username = jwtTokenProvider.getUsernameFromToken(refreshToken);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String newAccessToken = jwtTokenProvider.generateAccessToken(user.getUsername(), user.getUserId().toString());

        return TokenResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(refreshToken)
                .expiresIn(900L)
                .tokenType("Bearer")
                .build();
    }

public boolean validateToken(String token) {
    log.info("Checking token blacklist status...");
    
    if (blacklistedTokenRepository.existsById(token)) {
        log.warn("Validation failed: Token has been revoked via logout.");
        return false;
    }

    if (!jwtTokenProvider.validateToken(token)) {
        throw new RuntimeException("Token validation failed: Expired or altered signature");
    }

    return true;
}

    public void logout(String token) {
        java.time.LocalDateTime expiry = java.time.LocalDateTime.now().plusSeconds(900);

        BlacklistedToken blacklistedToken = BlacklistedToken.builder()
                                            .token(token)
                                            .expiryTime(expiry)
                                            .build();

        blacklistedTokenRepository.save(blacklistedToken);
        log.info("Token successfully pushed to PostgreSQL blacklist schema. Session killed.");
    }

    public void deleteUser(String userId) {
        UUID userUuid = UUID.fromString(userId);
        if (!userRepository.existsById(userUuid)) {
            throw new RuntimeException("User not found with ID: " + userId);
        }
        userRepository.deleteById(userUuid);
        log.info("User with ID '{}' successfully deleted from the database.", userId);
    }

    public void deleteRole(String roleId) {
        UUID roleUuid = UUID.fromString(roleId);
        if (!roleRepository.existsById(roleUuid)) {
            throw new RuntimeException("Role not found with ID: " + roleId);
        }
        
        if (roleRepository.isRoleInUse(roleUuid)) {
            throw new RuntimeException("Cannot delete role: This role is currently assigned to active users.");
        }
        
        roleRepository.deleteById(roleUuid);
        log.info("Role with ID '{}' successfully deleted from the database.", roleId);
    }
}
