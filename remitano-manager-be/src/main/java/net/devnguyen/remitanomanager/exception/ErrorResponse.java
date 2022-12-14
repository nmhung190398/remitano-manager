package net.devnguyen.remitanomanager.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import net.devnguyen.remitanomanager.webapp.response.ServiceResponse;

/**
 * Represent http response body
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse<T> extends ServiceResponse<T> {
    private String error;

    @Builder
    public ErrorResponse(int code, String message, T data, String error) {
        super(code, message, data);
        this.error = error;
    }
}
