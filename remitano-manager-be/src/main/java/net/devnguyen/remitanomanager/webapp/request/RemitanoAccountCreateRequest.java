package net.devnguyen.remitanomanager.webapp.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class RemitanoAccountCreateRequest {

    @NotNull
    private String accessKey;

    @NotNull
    private String secretKey;
}
