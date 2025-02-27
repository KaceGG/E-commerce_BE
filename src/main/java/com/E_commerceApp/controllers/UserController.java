//package com.E_commerceApp.controllers;
//
//import com.E_commerceApp.models.User;
//import com.E_commerceApp.services.UserService;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/user")
//@CrossOrigin("*")
//public class UserController {
//    private final UserService userService;
//
//    public UserController(UserService userService) {
//        this.userService = userService;
//    }
//
//    @PostMapping("/register")
//    public User register(@RequestBody UserCreationRequest request) {
//        return userService.createUser(request);
//    }
//}
