package com.payway.controllers;

import com.payway.exceptions.UnauthorizedException;
import com.payway.models.Generic500Response;
import com.payway.models.Unauthorized401Response;
import com.payway.services.PaymentService;
import com.payway.utils.JwtTokenUtil;
import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.Map;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;
    private final JwtTokenUtil jwtTokenUtil;

    public PaymentController(PaymentService paymentService, JwtTokenUtil jwtTokenUtil) {
        this.paymentService = paymentService;
        this.jwtTokenUtil =jwtTokenUtil;
    }

    @PostMapping(value = "/submitproof", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Submit payment proof",
            description = "Allows a user to submit a payment proof (PDF/JPG/PNG) along with optional metadata to update debts."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Payment proof processed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data or file format"),
            @ApiResponse(responseCode = "500", description = "Reset failed", content = @Content(schema = @Schema(implementation = Generic500Response.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = Unauthorized401Response.class)))
    })
    public ResponseEntity<?> submitPaymentProof(
            HttpServletRequest request,
            @RequestPart("file") MultipartFile file,
            @RequestParam("toOpName") String toOpName,
            @RequestParam(value = "details", required = false) String details) throws UnauthorizedException{

        String token = request.getHeader("X-OBSERVATORY-AUTH");

        try {
            Claims claims = jwtTokenUtil.validateToken(token);
            String username = claims.getSubject();
            String role = claims.get("role", String.class);
            String toOpId = paymentService.ToOpId(username);
            String sourceOpId = paymentService.SourceOpId(username);

            //αυτο ισως ειναι ανηκει στο authentication logic
            // Ensure the user is associated with an operator
            if (!"operator".equals(role)) {
                throw new UnauthorizedException("Only operators can submit payment proofs.");
            }

            paymentService.processPayment(sourceOpId, toOpId, file, details);

            return ResponseEntity.ok(Map.of("status", "success", "message", "Payment proof processed successfully."));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("status", "failed", "message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new Generic500Response().status("failed").info(e.getMessage()));
        }
    }
}

