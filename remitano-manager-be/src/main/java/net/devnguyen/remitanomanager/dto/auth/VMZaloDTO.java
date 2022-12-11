package net.devnguyen.remitanomanager.dto.auth;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class VMZaloDTO {
    private String id;

    private String name;

    private String email;

    private String birthday;

    private String gender;

    private JsonNode picture;

    private int error;
}
