package com.E_commerceApp.controllers;

import com.E_commerceApp.DTOs.request.ChangePasswordRequest;
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

    //    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/getAll")
    public ApiResponse<List<UserResponse>> getUsers() {
        ApiResponse<List<UserResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setResult(userService.getUsers());
        return apiResponse;
//        return ApiResponse.<List<UserResponse>>builder()
//                .result(userService.getUsers())
//                .build();
    }

    @GetMapping("/{userId}")
    public ApiResponse<UserResponse> getUser(@PathVariable String userId) {
        ApiResponse<UserResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(userService.getUser(userId));
        apiResponse.setMessage("Fetch user information successfully!");
        return apiResponse;
    }

    @PostMapping("/create")
    public ApiResponse<UserResponse> create(@RequestBody @Valid UserCreationRequest request) {
        ApiResponse<UserResponse> response = new ApiResponse<>();
        response.setResult(userService.createUser(request));
        return response;
//        return ApiResponse.<UserResponse>builder()
//                .result(userService.createUser(request))
//                .build();
    }

    @PutMapping("/update/{userId}")
    public ApiResponse<UserResponse> updateUser(@PathVariable String userId,
                                                @RequestBody UserUpdateRequest request) {
        ApiResponse<UserResponse> response = new ApiResponse<>();
        response.setResult(userService.updateUser(userId, request));
        return response;
//        return ApiResponse.<UserResponse>builder()
//                .result(userService.updateUser(userId, request))
//                .build();
    }

    @PostMapping("/{userId}/change-password")
    public ApiResponse<Void> changePassword(
            @PathVariable String userId,
            @RequestBody ChangePasswordRequest request) {
        userService.changePassword(userId, request);
        ApiResponse<Void> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Change password successfully");
        return apiResponse;
    }

    //    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("/delete/{userId}")
    public ApiResponse<Void> deleteUser(@PathVariable String userId) {
        userService.deleteUser(userId);
        ApiResponse<Void> response = new ApiResponse<>();
        response.setMessage("User deleted successfully!");
        return response;
//        return ApiResponse.<Void>builder()
//                .message("User has been deleted")
//                .build();
    }
}
