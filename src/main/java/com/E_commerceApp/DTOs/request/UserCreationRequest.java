package com.E_commerceApp.DTOs.request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserCreationRequest {
    @Size(min = 3, message = "Username must be at least 3 characters!")
    private String username;

    @Size(min = 8, message = "Password must be at least 8 characters!")
    private String password;
    private String fullName;
    private LocalDate birthday;
    private String email;
    private String phone;
    private String address;
}

