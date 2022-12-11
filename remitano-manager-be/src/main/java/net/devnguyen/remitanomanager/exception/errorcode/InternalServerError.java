package net.devnguyen.remitanomanager.exception.errorcode;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import net.devnguyen.remitanomanager.exception.ResponseError;
import org.springframework.http.HttpStatus;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@Getter
public enum InternalServerError implements ResponseError {
    INTERNAL_SERVER_ERROR("There are somethings wrong {0}"),
    ;

    InternalServerError(String message) {
        this.message = message;
    }

    private final String message;

    @Override
    public String getName() {
        return this.name();
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public int getStatus() {
        return HttpStatus.BAD_REQUEST.value();
    }
}
