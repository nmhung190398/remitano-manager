package net.devnguyen.remitanomanager.webapp.request;

import lombok.Data;
import net.devnguyen.remitanomanager.enums.VideoSourceType;

import javax.validation.constraints.NotNull;

@Data
public class CreateVideoRequest {
    @NotNull
    private String sourceId;
    @NotNull
    private VideoSourceType sourceType;
    @NotNull
    private String title;
    @NotNull
    private String description;
}
