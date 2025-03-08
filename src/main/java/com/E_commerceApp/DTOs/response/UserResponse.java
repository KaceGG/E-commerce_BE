package com.E_commerceApp.DTOs.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {
    private String id;
    private String username;
    private String password;
    private String fullName;
    private LocalDate birthday;
    private String email;
    private String phone;
    private String address;
}
