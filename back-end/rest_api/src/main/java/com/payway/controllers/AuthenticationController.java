package com.payway.controllers;

import com.payway.api.AuthenticationApi;
import com.payway.model.LoginPost200Response;
import com.payway.repositories.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AuthenticationController implements AuthenticationApi {
    private final UserRepository userRepository;

    public AuthenticationController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ResponseEntity<LoginPost200Response> loginPost(String username, String password) {
        System.out.println("Username: " + username + ", Password: " + password);
        // Check if user exists in the database
        return userRepository.findByUsernameAndPassword(username, password)
                .map(user -> {
                    LoginPost200Response response = new LoginPost200Response();
                    response.setToken("dummyToken123"); // Replace with actual token generation logic
                    return ResponseEntity.ok(response);
                })
                .orElse(ResponseEntity.status(401).build());
    }

    public ResponseEntity<Void> logoutPost() {
        // Invalidate token logic
        return ResponseEntity.ok().build();
    }
}
