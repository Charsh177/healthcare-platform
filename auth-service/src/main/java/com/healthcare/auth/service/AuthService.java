package com.healthcare.auth.service;

import com.healthcare.auth.dto.LoginRequest;
import com.healthcare.auth.dto.TokenResponse;
import com.healthcare.auth.dto.UserRegisterRequest;
import com.healthcare.auth.entity.User;
import com.healthcare.auth.entity.Role;
import com.healthcare.auth.repository.RoleRepository;
import com.healthcare.auth.repository.UserRepository;
import com.healthcare.auth.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
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

        Role role = roleRepository.findById(UUID.fromString(request.getRoleId()))
        .orElseThrow(() -> new RuntimeException("Role not found"));

        User user = User.builder()
                .username(request.getUsername())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .role(role)  // Assign the Role entity
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
}
