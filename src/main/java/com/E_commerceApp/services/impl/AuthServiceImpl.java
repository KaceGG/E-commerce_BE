package com.E_commerceApp.services.impl;

import com.E_commerceApp.DTOs.request.AuthRequest;
import com.E_commerceApp.exception.AppException;
import com.E_commerceApp.exception.ErrorCode;
import com.E_commerceApp.repositories.UserRepository;
import com.E_commerceApp.services.AuthService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;

    public AuthServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean authenticate(AuthRequest authRequest) {
        var user = userRepository.findByUsername(authRequest.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        return passwordEncoder.matches(authRequest.getPassword(), user.getPassword());
    }
}
