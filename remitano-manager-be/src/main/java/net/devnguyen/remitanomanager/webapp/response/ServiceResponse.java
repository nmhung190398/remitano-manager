package net.devnguyen.remitanomanager.webapp.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * @author nmhung-evotek on 6/28/2021
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ServiceResponse<T> {
    private int code;
    private String message;
    private T data;

    public static <T> ServiceResponse<T> succeed(HttpStatus status, T data) {
        return new ServiceResponse<>(status.value(), null, data);
    }

    public static <T> ServiceResponse<T> succeed(T data) {
        return new ServiceResponse<>(HttpStatus.OK.value(), null, data);
    }

    public static <T> ServiceResponse<T> succeed(HttpStatus status, String message, T data) {
        return new ServiceResponse<>(status.value(), message, data);
    }
}
