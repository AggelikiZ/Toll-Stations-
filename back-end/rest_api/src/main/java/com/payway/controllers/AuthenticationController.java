package com.payway.controllers;


import com.payway.models.LoginRequest;
import com.payway.repositories.UserRepository;
import com.payway.utils.JwtTokenUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.payway.models.User;
import com.payway.services.jwtBlackListService;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class AuthenticationController {
    @Value("${jwt.secret}") // Inject the secret key from application.properties
    private String SECRET_KEY;
    private final UserRepository userRepository;
    private final JwtTokenUtil jwtTokenUtil;
    private final jwtBlackListService jwtBlackListService;

    public AuthenticationController(UserRepository userRepository, JwtTokenUtil jwtTokenUtil, jwtBlackListService jwtBlackListService) {
        this.userRepository = userRepository;
        this.jwtTokenUtil = jwtTokenUtil;
        this.jwtBlackListService = jwtBlackListService;
    }

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "User Login",
            description = "Authenticate a user and return a token for accessing protected endpoints.",
            requestBody = @RequestBody(
                    description = "User credentials",
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
                            schema = @Schema(implementation = LoginRequest.class)
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Login successful", content = @Content(schema = @Schema(example = "{\"token\": \"FOO\"}"))),
                    @ApiResponse(responseCode = "400", description = "Invalid credentials", content = @Content(schema = @Schema(example = "{\"error\": \"Invalid credentials\"}")))
            }
    )
    public ResponseEntity<?> login(@ModelAttribute LoginRequest loginRequest) {
        try {
            String username = loginRequest.getUsername();
            String password = loginRequest.getPassword();

            User user = userRepository.findByUsernameAndPassword(username, password)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));
            System.out.println("User found with role " + user.getUserRole().name());
            String token = jwtTokenUtil.generateToken(username, user.getUserRole().name());
            System.out.println("Generated token: " + token);
            return ResponseEntity.ok(Map.of("token", token));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/logout")
    @Operation(
            summary = "Logout user",
            description = "Logs out the user by invalidating their token."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Logout successful"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(example = "{\"error\":\"Invalid or expired token\"}")))
    })
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        String token = request.getHeader("X-OBSERVATORY-AUTH");
        jwtBlackListService.addToBlacklist(token); // Προσθήκη του token στη blacklist

        return ResponseEntity.ok().build(); // Επιστροφή 200 OK με κενό σώμα
    }

//    @PostMapping(value = "/logout", produces = MediaType.APPLICATION_JSON_VALUE)
//    @Operation(
//            summary = "Logout User",
//            description = "Logs out the user by invalidating the provided token.",
//            responses = {
//                    @ApiResponse(responseCode = "200", description = "Logout successful"),
//                    @ApiResponse(responseCode = "401", description = "Unauthorized: Invalid or missing token",
//                            content = @Content(schema = @Schema(example = "{\"error\":\"Invalid or missing token\"}")))
//            }
//    )
//    public ResponseEntity<?> logout(@RequestHeader("X-OBSERVATORY-AUTH") String token) {
//        if (jwtBlackListService.addToBlacklist(token)) {
//            return ResponseEntity.ok().build();
//        }
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Invalid token"));
//    }

//    public ResponseEntity<?> logout(@RequestHeader(value = "X-OBSERVATORY-AUTH", required = false) String token) {
//        if (token == null || token.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//                    .body(Map.of("error", "Invalid or missing token"));
//        }
//
//        // Blacklist the token
//        jwtBlacklistService.blacklistToken(token);
//
//        // Return 200 OK
//        return ResponseEntity.ok().build();
//    }
//}
}
