package com.E_commerceApp.services;

import com.E_commerceApp.DTOs.request.ChangePasswordRequest;
import com.E_commerceApp.DTOs.request.UserCreationRequest;
import com.E_commerceApp.DTOs.request.UserUpdateRequest;
import com.E_commerceApp.DTOs.response.UserResponse;

import java.util.List;

public interface UserService {
    UserResponse getUser(String userId);

    List<UserResponse> getUsers();

    UserResponse createUser(UserCreationRequest request);

    UserResponse updateUser(String userId, UserUpdateRequest request);

    void deleteUser(String userId);

    void changePassword(String userId, ChangePasswordRequest request);
}