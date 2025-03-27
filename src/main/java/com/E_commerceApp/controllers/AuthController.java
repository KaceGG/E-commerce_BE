package com.E_commerceApp.controllers;

import com.E_commerceApp.DTOs.request.AuthRequest;
import com.E_commerceApp.DTOs.response.ApiResponse;
import com.E_commerceApp.DTOs.response.AuthResponse;
import com.E_commerceApp.services.AuthService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin("*")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ApiResponse<AuthResponse> authenticate(@RequestBody AuthRequest request) {
        var result = authService.authenticate(request);
        return ApiResponse.<AuthResponse>builder()
                .result(result)
                .build();
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout() {
        authService.logout();
        ApiResponse<Void> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Logout successfully!");
        return apiResponse;
    }
}
