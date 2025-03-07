package com.E_commerceApp.services.impl;

import com.E_commerceApp.DTOs.request.UserCreationRequest;
import com.E_commerceApp.DTOs.request.UserUpdateRequest;
import com.E_commerceApp.exception.AppException;
import com.E_commerceApp.exception.ErrorCode;
import com.E_commerceApp.models.User;
import com.E_commerceApp.repositories.UserRepository;
import com.E_commerceApp.services.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User getUser(String userId) {
        return userRepository.findById(userId).orElseThrow(()
                -> new AppException(ErrorCode.USER_NOT_FOUND));
    }

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public User createUser(UserCreationRequest request) {
        User user = new User();
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setFullName(request.getFullName());
        user.setBirthday(request.getBirthday());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setAddress(request.getAddress());
        userRepository.save(user);
        return user;
    }

    @Override
    public User updateUser(String userId, UserUpdateRequest request) {
        User user = userRepository.findById(userId).orElseThrow(()
                -> new AppException(ErrorCode.USER_NOT_FOUND));
        user.setPassword(request.getPassword());
        user.setFullName(request.getFullName());
        user.setBirthday(request.getBirthday());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setAddress(request.getAddress());
        userRepository.save(user);
        return user;
    }

    @Override
    public void deleteUser(String userId) {
        User user = userRepository.findById(userId).orElseThrow(()
                -> new AppException(ErrorCode.USER_NOT_FOUND));
        userRepository.delete(user);
    }
}
