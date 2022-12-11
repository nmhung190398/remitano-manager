package net.devnguyen.remitanomanager.exception;

public interface ResponseError {
    String getName();

    String getMessage();

    int getStatus();

    default Integer getCode() {
        return 0;
    }


    default ResponseException exception(Object... params) {
        return new ResponseException(this, params);
    }
}
