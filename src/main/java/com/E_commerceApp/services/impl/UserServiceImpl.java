package com.E_commerceApp.services.impl;

import com.E_commerceApp.repositories.UserRepository;
import com.E_commerceApp.services.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

//    @Override
//    public User createUser(UserCreationRequest request) {
//        User user = new User();
//        if (userRepository.existsByUsername(request.getUsername())) {
//            throw new RuntimeException("Username already exists");
//        }
//        user.setUsername(request.getUsername());
//        user.setEmail(request.getEmail());
//        userRepository.save(user);
//        return user;
//    }
}
