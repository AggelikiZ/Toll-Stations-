/**
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech) (7.0.1).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */
package com.payway.api;

import com.payway.model.LoginPost200Response;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import jakarta.annotation.Generated;

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2024-12-29T16:43:21.221861+02:00[Europe/Athens]")
@Validated
@Tag(name = "Authentication", description = "the Authentication API")
public interface AuthenticationApi {

    default Optional<NativeWebRequest> getRequest() {
        return Optional.empty();
    }

    /**
     * POST /login : User login
     *
     * @param username User&#39;s username (optional)
     * @param password User&#39;s password (optional)
     * @return Successful login (status code 200)
     *         or Unauthorized access (status code 401)
     */
    @Operation(
        operationId = "loginPost",
        summary = "User login",
        tags = { "Authentication" },
        responses = {
            @ApiResponse(responseCode = "200", description = "Successful login", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = LoginPost200Response.class))
            }),
            @ApiResponse(responseCode = "401", description = "Unauthorized access")
        }
    )
    @RequestMapping(
        method = RequestMethod.POST,
        value = "/login",
        produces = { "application/json" },
        consumes = { "application/x-www-form-urlencoded" }
    )
    default ResponseEntity<LoginPost200Response> loginPost(
        @Parameter(name = "username", description = "User's username") @Valid @RequestParam(value = "username", required = false) String username,
        @Parameter(name = "password", description = "User's password") @Valid @RequestParam(value = "password", required = false) String password
    ) {
        getRequest().ifPresent(request -> {
            for (MediaType mediaType: MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "{ \"token\" : \"token\" }";
                    ApiUtil.setExampleResponse(request, "application/json", exampleString);
                    break;
                }
            }
        });
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }


    /**
     * POST /logout : User logout
     *
     * @return Successful logout (status code 200)
     *         or Unauthorized access (status code 401)
     */
    @Operation(
        operationId = "logoutPost",
        summary = "User logout",
        tags = { "Authentication" },
        responses = {
            @ApiResponse(responseCode = "200", description = "Successful logout"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access")
        }
    )
    @RequestMapping(
        method = RequestMethod.POST,
        value = "/logout"
    )
    default ResponseEntity<Void> logoutPost(
        
    ) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }

}
