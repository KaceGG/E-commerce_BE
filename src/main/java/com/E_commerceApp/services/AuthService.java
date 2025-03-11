package com.E_commerceApp.services;

import com.E_commerceApp.DTOs.request.AuthRequest;

public interface AuthService {
    boolean authenticate(AuthRequest authRequest);
}
