package net.devnguyen.remitanomanager.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseException extends RuntimeException {
    private ResponseError error;
    private Object[] params;
    private Object data;

    public ResponseException(ResponseError error, Object... params) {
        this(null, error.getMessage(), error, params);
    }

    public ResponseException(Object data, String message, ResponseError error, Object... params) {
        this(data, message, null, error, params);
    }

    public ResponseException(String message, ResponseError error, Object... params) {
        this(null, message, null, error, params);
    }


    public ResponseException(Object data, String message, Throwable cause, ResponseError error, Object... params) {
        super(message, cause);
        this.error = error;
        this.params = params == null ? new Object[]{} : params;
        this.data = data;
    }
}
