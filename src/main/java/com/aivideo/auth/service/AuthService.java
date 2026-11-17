package com.aivideo.auth.service;

import com.aivideo.auth.dto.ApiKeyRequest;
import com.aivideo.auth.dto.AuthResponse;
import com.aivideo.auth.dto.LoginRequest;
import com.aivideo.auth.dto.RegisterRequest;
import com.aivideo.auth.jwt.JwtTokenProvider;
import com.aivideo.common.exception.ResourceNotFoundException;
import com.aivideo.common.util.EncryptionUtil;
import com.aivideo.user.entity.User;
import com.aivideo.user.entity.UserApiKey;
import com.aivideo.user.repository.UserApiKeyRepository;
import com.aivideo.user.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.util.UUID;

@Service
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    private final UserRepository userRepository;
    private final UserApiKeyRepository userApiKeyRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;
    private final EncryptionUtil encryptionUtil;

    public AuthService(UserRepository userRepository,
                       UserApiKeyRepository userApiKeyRepository,
                       PasswordEncoder passwordEncoder,
                       JwtTokenProvider tokenProvider,
                       EncryptionUtil encryptionUtil) {
        this.userRepository = userRepository;
        this.userApiKeyRepository = userApiKeyRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
        this.encryptionUtil = encryptionUtil;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        log.info("Processing user registration request for email: [{}]", request.getEmail());

        if (userRepository.existsByEmail(request.getEmail())) {
            log.warn("Registration rejected: email [{}] is already registered", request.getEmail());
            throw new IllegalArgumentException("Email already registered: " + request.getEmail());
        }

        User user = User.builder()
                .email(request.getEmail().toLowerCase().trim())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .displayName(request.getDisplayName())
                .role("USER")
                .isActive(true)
                .build();

        user = userRepository.save(user);
        log.info("User registered successfully with ID: [{}]", user.getId());

        boolean hasKey = false;
        if (StringUtils.hasText(request.getGeminiApiKey())) {
            saveOrUpdateApiKey(user, "GEMINI", request.getGeminiApiKey());
            hasKey = true;
        }

        String token = tokenProvider.generateToken(user.getId(), user.getEmail(), user.getRole());

        return AuthResponse.builder()
                .token(token)
                .tokenType("Bearer")
                .userId(user.getId())
                .email(user.getEmail())
                .displayName(user.getDisplayName())
                .role(user.getRole())
                .hasGeminiApiKey(hasKey)
                .build();
    }

    @Transactional
    public AuthResponse login(LoginRequest request) {
        log.info("Processing login attempt for email: [{}]", request.getEmail());

        User user = userRepository.findByEmail(request.getEmail().toLowerCase().trim())
                .orElseThrow(() -> {
                    log.warn("Login failed: no account found with email [{}]", request.getEmail());
                    return new BadCredentialsException("Invalid email or password");
                });

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            log.warn("Login failed: invalid password for user ID [{}]", user.getId());
            throw new BadCredentialsException("Invalid email or password");
        }

        user.setLastLoginAt(Instant.now());
        userRepository.save(user);
        log.info("User login successful for user ID: [{}]", user.getId());

        boolean hasKey = userApiKeyRepository.findByUserIdAndProvider(user.getId(), "GEMINI").isPresent();
        String token = tokenProvider.generateToken(user.getId(), user.getEmail(), user.getRole());

        return AuthResponse.builder()
                .token(token)
                .tokenType("Bearer")
                .userId(user.getId())
                .email(user.getEmail())
                .displayName(user.getDisplayName())
                .role(user.getRole())
                .hasGeminiApiKey(hasKey)
                .build();
    }

    @Transactional
    public void saveOrUpdateApiKey(User user, String provider, String plainKey) {
        log.info("Saving encrypted API key for provider [{}] for user ID [{}]", provider, user.getId());
        String encrypted = encryptionUtil.encrypt(plainKey);
        UserApiKey apiKey = userApiKeyRepository.findByUserIdAndProvider(user.getId(), provider)
                .orElse(UserApiKey.builder()
                        .user(user)
                        .provider(provider)
                        .build());

        apiKey.setEncryptedKey(encrypted);
        apiKey.setIsActive(true);
        userApiKeyRepository.save(apiKey);
        log.info("API key saved and encrypted successfully for provider [{}]", provider);
    }

    @Transactional
    public void setApiKey(UUID userId, ApiKeyRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        saveOrUpdateApiKey(user, request.getProvider(), request.getApiKey());
    }

    @Transactional(readOnly = true)
    public String getDecryptedApiKey(UUID userId, String provider) {
        return userApiKeyRepository.findByUserIdAndProvider(userId, provider)
                .map(UserApiKey::getEncryptedKey)
                .map(encryptionUtil::decrypt)
                .orElse(null);
    }
}
