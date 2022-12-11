package net.devnguyen.remitanomanager.exception.errorcode;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import net.devnguyen.remitanomanager.exception.ResponseError;
import org.springframework.http.HttpStatus;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@Getter
public enum AccessDeniedError implements ResponseError {
    ACCESS_DENIED(4030000, "You don't have permission to access this"),
    NOT_SUPPORTED_AUTHENTICATION(4030001, "Your authentication has not been supported yet: {0}"),
    NOT_PERMITTED_ACCESS(4030002, "Need permission to access: {0}"),
    USER_NOT_FOUND(4030003, "user not found"),
    PASSWORD_WRONG(4030004, "Password wrong"),
    EMAIL_EXISTED(4030005, "Email existed"),
    USER_NOT_ACTIVE(4030006, "USER_NOT_ACTIVE"),

    ;

    private final int errorCode;
    private final String message;

    AccessDeniedError(int errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    @Override
    public String getName() {
        return this.name();
    }

    public int getStatus() {
        return HttpStatus.FORBIDDEN.value();
    }

    @Override
    public Integer getCode() {
        return errorCode;
    }
}
