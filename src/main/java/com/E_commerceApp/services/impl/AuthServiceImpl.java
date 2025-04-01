package com.E_commerceApp.services.impl;

import com.E_commerceApp.DTOs.request.AuthRequest;
import com.E_commerceApp.DTOs.response.AuthResponse;
import com.E_commerceApp.exception.AppException;
import com.E_commerceApp.exception.ErrorCode;
import com.E_commerceApp.models.Role;
import com.E_commerceApp.models.User;
import com.E_commerceApp.repositories.UserRepository;
import com.E_commerceApp.services.AuthService;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    @Value("${spring.jwt.signerKey}")
    private String signerKey;

    public AuthServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public AuthResponse authenticate(AuthRequest request) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        boolean authenticated = passwordEncoder.matches(request.getPassword(),
                user.getPassword());

        if (!authenticated)
            throw new AppException(ErrorCode.LOGIN_FAILED);

//        var token = generateToken(request.getUsername());
        var token = generateToken(user);

        return AuthResponse.builder()
                .token(token)
                .build();
    }

    @Override
    public void logout() {
    }

    @Override
    public String generateToken(User user) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
        List<String> roles = user.getRoles().stream()
                .map(Role::getName)
                .toList();

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getId())
                .issueTime(new Date())
                .expirationTime(new Date(
                        new Date().getTime() + 3600 * 1000
                ))
                .claim("username", user.getUsername())
                .claim("roles", roles)
                .build();
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(header, payload);
        try {
            jwsObject.sign(new MACSigner(signerKey.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }
}
