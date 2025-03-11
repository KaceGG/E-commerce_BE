package com.E_commerceApp.services.impl;

import com.E_commerceApp.DTOs.request.UserCreationRequest;
import com.E_commerceApp.DTOs.request.UserUpdateRequest;
import com.E_commerceApp.DTOs.response.UserResponse;
import com.E_commerceApp.exception.AppException;
import com.E_commerceApp.exception.ErrorCode;
import com.E_commerceApp.mappers.UserMapper;
import com.E_commerceApp.models.User;
import com.E_commerceApp.repositories.UserRepository;
import com.E_commerceApp.services.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public UserResponse getUser(String userId) {
        return userMapper.toUserResponse(userRepository.findById(userId).orElseThrow(()
                -> new AppException(ErrorCode.USER_NOT_FOUND)));
    }

    @Override
    public List<UserResponse> getUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toUserResponse).collect(Collectors.toList());
    }

    @Override
    public UserResponse createUser(UserCreationRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        User user = userMapper.toUser(request);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userMapper.toUserResponse(userRepository.save(user));
    }

    @Override
    public UserResponse updateUser(String userId, UserUpdateRequest request) {
        User user = userRepository.findById(userId).orElseThrow(()
                -> new AppException(ErrorCode.USER_NOT_FOUND));
        userMapper.updateUser(user, request);
        return userMapper.toUserResponse(userRepository.save(user));
    }

    @Override
    public void deleteUser(String userId) {
        User user = userRepository.findById(userId).orElseThrow(()
                -> new AppException(ErrorCode.USER_NOT_FOUND));
        userRepository.delete(user);
    }
}
