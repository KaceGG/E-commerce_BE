package com.E_commerceApp.services;

import com.E_commerceApp.DTOs.request.AuthRequest;
import com.E_commerceApp.DTOs.response.AuthResponse;
import com.E_commerceApp.models.User;

public interface AuthService {
    AuthResponse authenticate(AuthRequest authRequest);

    String generateToken(User user);
}
