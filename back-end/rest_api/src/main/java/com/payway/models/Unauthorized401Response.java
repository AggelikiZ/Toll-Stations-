package com.payway.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Unauthorized_401_response")
public class Unauthorized401Response {

    @Schema(name = "status", example = "failed")
    @JsonProperty("status")
    private String status;

    @Schema(name = "info", example = "Invalid or expired token")
    @JsonProperty("info")
    private String info;

    public Unauthorized401Response() {}

    public Unauthorized401Response(String status, String info) {
        this.status = status;
        this.info = info;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
