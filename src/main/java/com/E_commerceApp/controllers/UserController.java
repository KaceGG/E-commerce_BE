package com.E_commerceApp.controllers;

import com.E_commerceApp.DTOs.request.UserCreationRequest;
import com.E_commerceApp.DTOs.request.UserUpdateRequest;
import com.E_commerceApp.DTOs.response.ApiResponse;
import com.E_commerceApp.DTOs.response.UserResponse;
import com.E_commerceApp.services.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@CrossOrigin("*")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/getAll")
    public ApiResponse<List<UserResponse>> getUsers() {
        ApiResponse<List<UserResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setResult(userService.getUsers());
        return apiResponse;
    }

    @GetMapping("/{userId}")
    public UserResponse getUser(@PathVariable String userId) {
        return userService.getUser(userId);
    }

    @PostMapping("/create")
    public ApiResponse<UserResponse> create(@RequestBody @Valid UserCreationRequest request) {
        ApiResponse<UserResponse> response = new ApiResponse<>();
        response.setResult(userService.createUser(request));
        return response;
    }

    @PutMapping("/update/{userId}")
    public ApiResponse<UserResponse> updateUser(@PathVariable String userId,
                                                @RequestBody UserUpdateRequest request) {
        ApiResponse<UserResponse> response = new ApiResponse<>();
        response.setResult(userService.updateUser(userId, request));
        return response;
    }

    @DeleteMapping("/delete/{userId}")
    public ApiResponse<Void> deleteUser(@PathVariable String userId) {
        userService.deleteUser(userId);
        ApiResponse<Void> response = new ApiResponse<>();
        response.setMessage("User deleted successfully!");
        return response;
    }
}
