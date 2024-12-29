package com.payway.controllers;

import com.payway.api.AuthenticationApi;
import com.payway.model.LoginPost200Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController implements AuthenticationApi {

    @Override
    public ResponseEntity<LoginPost200Response> loginPost(String username, String password) {
        // Add your login logic here (e.g., validate user credentials)
        if ("admin".equals(username) && "password".equals(password)) {
            // Create a successful response
            LoginPost200Response response = new LoginPost200Response();
            response.setToken("dummyToken123"); // Replace with actual token generation logic
            return ResponseEntity.ok(response);
        } else {
            // Return unauthorized response
            return ResponseEntity.status(401).build();
        }
    }

    @Override
    public ResponseEntity<Void> logoutPost() {
        // Add your logout logic here (e.g., invalidate the token)
        return ResponseEntity.ok().build();
    }
}
