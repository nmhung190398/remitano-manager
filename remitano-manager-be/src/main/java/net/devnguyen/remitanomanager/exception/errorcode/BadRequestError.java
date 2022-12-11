package net.devnguyen.remitanomanager.exception.errorcode;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import net.devnguyen.remitanomanager.exception.ResponseError;
import org.springframework.http.HttpStatus;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@Getter
public enum BadRequestError implements ResponseError {

    INVALID_INPUT(4000000, "INVALID_INPUT : {0}"),
    INVALID_ACCEPT_LANGUAGE(4000001, "Invalid value for request header Accept-Language: {0}"),
    MISSING_PATH_VARIABLE(4000002, "Missing path variable"), //MissingPathVariable
    PATH_INVALID(4000003, "Path is invalid"),
    USER_MUST_BELONG_A_COMPANY(4000004, "You must belong a company"),
    VIDEO_EXISTED(4000004, "VIDEO_EXISTED"),
    DUPLICATE_ACCOUNT(4000005, "Account existed"),
    ;

    private final int errorCode;
    private final String message;

    BadRequestError(int errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    @Override
    public String getName() {
        return this.name();
    }

    @Override
    public int getStatus() {
        return HttpStatus.BAD_REQUEST.value();
    }
}
