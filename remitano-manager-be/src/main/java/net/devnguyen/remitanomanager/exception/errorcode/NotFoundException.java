package net.devnguyen.remitanomanager.exception.errorcode;

import lombok.Getter;
import net.devnguyen.remitanomanager.exception.ResponseError;

/**
 * @author nmhung-evotek on 6/28/2021
 */
@Getter
public enum NotFoundException implements ResponseError {

    RECORD_NOT_FOUND(404000, "SCOPE_NOT_FOUND"),
    SCOPE_NOT_FOUND(404001, "SCOPE_NOT_FOUND"),
    USER_NOT_FOUND(404002, "USER_NOT_FOUND"),
    VIDEO_YOUTUBE_INVALID(404003, "VIDEO_YOUTUBE_INVALID"),
    BOX_TIME_VIDEO_NOT_FOUND(404004, "BOX_TIME_VIDEO_NOT_FOUND"),
    ;

    private final String message;
    private final int code;

    NotFoundException(int code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getName() {
        return name();
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public int getStatus() {
        return 400;
    }

    public Integer getCode() {
        return code;
    }
}
