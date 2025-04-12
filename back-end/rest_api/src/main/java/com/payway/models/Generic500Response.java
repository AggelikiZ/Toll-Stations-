package com.payway.models;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.swagger.v3.oas.annotations.media.Schema;

@JsonTypeName("Generic_500_response")
public class Generic500Response {

    private String status;

    private String info;

    public Generic500Response status(String status) {
        this.status = status;
        return this;
    }

    /**
     * Get status
     * @return status
     */

    @Schema(name = "status", example = "failed", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("status")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Generic500Response info(String info) {
        this.info = info;
        return this;
    }

    /**
     * Get info
     * @return info
     */

    @Schema(name = "info", example = "Unexpected internal server error", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("info")
    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Generic500Response Generic500Response = (Generic500Response) o;
        return Objects.equals(this.status, Generic500Response.status) &&
                Objects.equals(this.info, Generic500Response.info);
    }

    @Override
    public int hashCode() {
        return Objects.hash(status, info);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Generic500Response {\n");
        sb.append("    status: ").append(toIndentedString(status)).append("\n");
        sb.append("    info: ").append(toIndentedString(info)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    private String toIndentedString(Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }
}

