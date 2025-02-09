package com.payway.controllers;

import com.payway.models.LoginRequest;
import com.payway.models.Operator;
import com.payway.repositories.UserRepository;
import com.payway.repositories.OperatorRepository;
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

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api")
public class AuthenticationController {
    @Value("${jwt.secret}") // Inject the secret key from application.properties
    private String SECRET_KEY;
    private final UserRepository userRepository;
    private final JwtTokenUtil jwtTokenUtil;
    private final OperatorRepository operatorRepository;
    private final jwtBlackListService jwtBlackListService;

    public AuthenticationController(UserRepository userRepository, JwtTokenUtil jwtTokenUtil, jwtBlackListService jwtBlackListService, OperatorRepository operatorRepository) {
        this.userRepository = userRepository;
        this.jwtTokenUtil = jwtTokenUtil;
        this.jwtBlackListService = jwtBlackListService;
        this.operatorRepository = operatorRepository;
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


    @GetMapping("/auth/operatorId")
    @Operation(
            summary = "Get Operator ID and Role",
            description = "Retrieves the operator ID (opId) and role of the logged-in user."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Operator ID and role retrieved successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(example = "{\"opId\": \"EG\", \"role\": \"admin\"}"))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(example = "{\"error\": \"Invalid or expired token\"}")))
    })
    public ResponseEntity<?> getOperatorId(HttpServletRequest request) {
        try {
            String token = request.getHeader("X-OBSERVATORY-AUTH");
            if (token == null || token.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Token not provided."));
            }

            // Verify token
            if (jwtBlackListService.isBlacklisted(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Token is invalid or blacklisted."));
            }

            // Extract username from the token
            String username = jwtTokenUtil.validateToken(token).getSubject();

            // Find the user by username
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new IllegalArgumentException("User not found."));

            // Get the user role
            String role = user.getUserRoleAsString(); // Convert enum to String

            // Try to find an operator for this user
            Optional<Operator> operatorOptional = operatorRepository.findByUserId(user.getId());

            // Prepare response
            Map<String, Object> response = new HashMap<>();
            response.put("role", role); // Always return role
            response.put("opId", operatorOptional.map(Operator::getOpId).orElse(null)); // Operator ID if available

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Failed to retrieve user data. " + e.getMessage()));
        }
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
