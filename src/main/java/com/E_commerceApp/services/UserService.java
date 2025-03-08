package com.E_commerceApp.services;

import com.E_commerceApp.DTOs.request.UserCreationRequest;
import com.E_commerceApp.DTOs.request.UserUpdateRequest;
import com.E_commerceApp.DTOs.response.UserResponse;

import java.util.List;

public interface UserService {
    public UserResponse getUser(String userId);

    public List<UserResponse> getUsers();

    public UserResponse createUser(UserCreationRequest request);

    public UserResponse updateUser(String userId, UserUpdateRequest request);

    public void deleteUser(String userId);
}
