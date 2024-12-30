package com.payway.controllers;


import com.payway.repositories.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class AuthenticationController {
//    private final UserRepository userRepository;
//
//    public AuthenticationController(UserRepository userRepository) {
//        this.userRepository = userRepository;
//    }
//
//    @PostMapping("/login")
//    public ResponseEntity<?> login(String username, String password) {
//        System.out.println("Username: " + username + ", Password: " + password);
//        // Check if user exists in the database
//        return userRepository.findByUsernameAndPassword(username, password)
//                .map(user -> {
//                    //response.setToken("dummyToken123"); // Replace with actual token generation logic
//                    return ResponseEntity.ok();
//                })
//                .orElse(ResponseEntity.status(401).build());
//    }
//
//    public ResponseEntity<Void> logoutPost() {
//        // Invalidate token logic
//        return ResponseEntity.ok().build();
//    }
}
