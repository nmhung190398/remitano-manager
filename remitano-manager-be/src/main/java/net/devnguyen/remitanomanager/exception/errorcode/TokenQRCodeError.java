package net.devnguyen.remitanomanager.exception.errorcode;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import net.devnguyen.remitanomanager.exception.ResponseError;
import org.springframework.http.HttpStatus;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@Getter
public enum TokenQRCodeError implements ResponseError {
    QR_CODE_IS_REQUIRED(4020001, "QR_CODE_IS_REQUIRED"),
    MALFORMED_QR_CODE(4020002, "MALFORMED_QR_CODE"),
    EXPIRED_QR_CODE(4020003, "EXPIRED_QR_CODE"),
    UNSUPPORTED_QR_CODE(4020004, "UNSUPPORTED_QR_CODE"),
    ;

    private final int errorCode;
    private final String message;

    TokenQRCodeError(int errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    @Override
    public String getName() {
        return this.name();
    }

    public int getStatus() {
        return HttpStatus.BAD_REQUEST.value();
    }

    @Override
    public Integer getCode() {
        return errorCode;
    }
}
