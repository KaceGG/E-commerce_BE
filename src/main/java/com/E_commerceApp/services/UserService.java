package com.E_commerceApp.services;

import com.E_commerceApp.DTOs.request.UserCreationRequest;
import com.E_commerceApp.DTOs.request.UserUpdateRequest;
import com.E_commerceApp.models.User;

import java.util.List;

public interface UserService {
    public User getUser(String userId);
    public List<User> getUsers();
    public User createUser(UserCreationRequest request);
    public User updateUser(String userId,UserUpdateRequest request);
    public void deleteUser(String userId);
}
