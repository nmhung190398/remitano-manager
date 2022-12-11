package net.devnguyen.remitanomanager.exception.errorcode;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import net.devnguyen.remitanomanager.exception.ResponseError;
import org.springframework.http.HttpStatus;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@Getter
public enum UnauthorizedError implements ResponseError {
    AUTHENTICATION_IS_REQUIRED(4010000, "Null or not supported authentication"),
    FORBIDDEN_ACCESS_TOKEN(4010001, "Access token has been forbidden due to user has logged out or deactivated"),
    MALFORMED_JWT(4010002, "MALFORMED_JWT"),
    EXPIRED_JWT(4010003, "EXPIRED_JWT"),
    UNSUPPORTED_JWT(4010004, "UNSUPPORTED_JWT"),
    AUTHORIZED_TOKEN_BY_CODE_ERROR(4010005, "AUTHORIZED_TOKEN_BY_CODE_ERROR"),
    VALIDATE_TOKEN_ERROR(4010006, "VALIDATE_TOKEN_ERROR"),
    ;

    private final int errorCode;
    private final String message;

    UnauthorizedError(int errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    @Override
    public String getName() {
        return this.name();
    }

    public int getStatus() {
        return HttpStatus.UNAUTHORIZED.value();
    }

    @Override
    public Integer getCode() {
        return errorCode;
    }
}
