package com.payway.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

public class Generic400Response {
    @Schema(description = "Error message", example = "Invalid parameters")
    private String message;

    @Schema(description = "Error details", example = "Missing 'id' field")
    private String error;
}
