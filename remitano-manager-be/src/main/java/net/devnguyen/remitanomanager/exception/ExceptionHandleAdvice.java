package net.devnguyen.remitanomanager.exception;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import lombok.extern.slf4j.Slf4j;
import net.devnguyen.remitanomanager.configuration.internationalization.LanguageService;
import net.devnguyen.remitanomanager.exception.errorcode.AccessDeniedError;
import net.devnguyen.remitanomanager.exception.errorcode.BadRequestError;
import net.devnguyen.remitanomanager.exception.errorcode.InternalServerError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.NonTransientDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.AccessDeniedException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
public class ExceptionHandleAdvice {

    private final LanguageService languageService;

    @Autowired
    public ExceptionHandleAdvice(LanguageService languageService) {
        this.languageService = languageService;
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse<Void>> handleMethodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException e, HttpServletRequest request) {
        log.warn("Failed to handle request " + request.getRequestURI() + ": " + e.getMessage());
        Set<FieldErrorResponse> errors = new HashSet<>();
        errors.add(FieldErrorResponse.builder()
                .field(e.getParameter().getParameterName())
                .message(e.getMessage()).build());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new InvalidInputResponse(
                        HttpStatus.BAD_REQUEST.value(),
                        e.getMessage(),
                        BadRequestError.INVALID_INPUT.getName(),
                        errors));
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ErrorResponse<Void>> handleIllegalStateException(
            MaxUploadSizeExceededException e, HttpServletRequest request) {
        Set<FieldErrorResponse> errors = new HashSet<>();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new InvalidInputResponse(
                        HttpStatus.BAD_REQUEST.value(),
                        e.getMessage(),
                        BadRequestError.INVALID_INPUT.getName(),
                        errors));
    }

    @ExceptionHandler(MissingPathVariableException.class)
    public ResponseEntity<ErrorResponse<Void>> handleMissingPathVariableException(
            MissingPathVariableException e, HttpServletRequest request) {
        log.warn("Failed to handle request " + request.getRequestURI() + ": " + e.getMessage());
        Set<FieldErrorResponse> errors = new HashSet<>();
        errors.add(FieldErrorResponse.builder()
                .field(e.getParameter().getParameterName())
                .message(e.getMessage()).build());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new InvalidInputResponse(
                        BadRequestError.MISSING_PATH_VARIABLE.getErrorCode(),
                        e.getMessage(),
                        BadRequestError.MISSING_PATH_VARIABLE.getName(),
                        errors));
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<ErrorResponse<Void>> handleMissingRequestHeaderException(
            MissingRequestHeaderException e, HttpServletRequest request) {
        BigDecimal a;
        log.warn("Failed to handle request " + request.getRequestURI() + ": " + e.getMessage());
        Set<FieldErrorResponse> errors = new HashSet<>();
        errors.add(FieldErrorResponse.builder()
                .field(e.getParameter().getParameterName())
                .message(e.getMessage()).build());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new InvalidInputResponse(
                        BadRequestError.MISSING_PATH_VARIABLE.getErrorCode(),
                        e.getMessage(),
                        BadRequestError.MISSING_PATH_VARIABLE.getName(),
                        errors));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse<Void>> handleRequestMethodNotSupportedException(
            HttpRequestMethodNotSupportedException e, HttpServletRequest request) {
        log.warn("Failed to handle request " + request.getRequestURI() + ": " + e.getMessage());
        Set<FieldErrorResponse> errors = new HashSet<>();
        errors.add(FieldErrorResponse.builder()
                .field(e.getMethod())
                .message(e.getMessage()).build());
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(new InvalidInputResponse(
                        HttpStatus.METHOD_NOT_ALLOWED.value(),
                        e.getMessage(),
                        BadRequestError.INVALID_INPUT.getName(),
                        errors));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<InvalidInputResponse> handleValidationException(
            ConstraintViolationException e, HttpServletRequest request) {
        String queryParam;
        String object;
        String errorMessage;
        Set<FieldErrorResponse> errors = new HashSet<>();
        for (ConstraintViolation constraintViolation : e.getConstraintViolations()) {
            String queryParamPath = constraintViolation.getPropertyPath().toString();
            log.debug("queryParamPath = {}", queryParamPath);
            queryParam = queryParamPath.contains(".") ?
                    queryParamPath.substring(queryParamPath.indexOf(".") + 1) :
                    queryParamPath;
            object = queryParamPath.split("\\.").length > 1 ?
                    queryParamPath.substring(queryParamPath.indexOf(".") + 1, queryParamPath.lastIndexOf(".")) :
                    queryParamPath;
            errorMessage = languageService.getMessage(
                    constraintViolation.getMessage(), constraintViolation.getMessage());
            errors.add(FieldErrorResponse.builder()
                    .field(queryParam)
                    .objectName(object)
                    .message(errorMessage).build());
        }
        InvalidInputResponse invalidInputResponse;
        if (errors.size() >= 1) {
            long count = errors.size();
            invalidInputResponse = errors.stream().skip(count - 1).findFirst()
                    .map(fieldErrorResponse -> new InvalidInputResponse(
                            HttpStatus.BAD_REQUEST.value(),
                            fieldErrorResponse.getMessage(),
                            fieldErrorResponse.getObjectName(),
                            errors))
                    .orElse(null);
        } else {
            invalidInputResponse = new InvalidInputResponse(
                    HttpStatus.BAD_REQUEST.value(),
                    languageService.getMessage(
                            BadRequestError.INVALID_INPUT.getMessage(),
                            "Invalid request arguments"),
                    BadRequestError.INVALID_INPUT.getName(),
                    errors);
        }
        log.warn("Failed to handle request " + request.getRequestURI() + ": " + e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(invalidInputResponse);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<InvalidInputResponse> handleValidationException(
            HttpMessageNotReadableException e, HttpServletRequest request) throws IOException {
        log.warn("Failed to handle request " + request.getRequestURI() + ": " + e.getMessage(), e);
        Throwable cause = e.getCause();
        InvalidInputResponse invalidInputResponse = null;
        if (cause instanceof InvalidFormatException) {
            InvalidFormatException invalidFormatException = (InvalidFormatException) cause;
            String fieldPath = invalidFormatException.getPath().stream()
                    .map(JsonMappingException.Reference::getFieldName)
                    .collect(Collectors.joining("."));
            invalidInputResponse = new InvalidInputResponse(
                    HttpStatus.BAD_REQUEST.value(),
                    e.getMessage(),
                    BadRequestError.INVALID_INPUT.name(),
                    Collections.singleton(FieldErrorResponse.builder()
                            .field(fieldPath)
                            .message("Invalid input format").build())
            );
        } else if (cause instanceof JsonParseException) {
            JsonParseException jsonParseException = (JsonParseException) cause;
            invalidInputResponse = new InvalidInputResponse(
                    HttpStatus.BAD_REQUEST.value(),
                    BadRequestError.INVALID_INPUT.getMessage(),
                    BadRequestError.INVALID_INPUT.name(),
                    Collections.singleton(FieldErrorResponse.builder()
                            .field(jsonParseException.getProcessor().getCurrentName())
                            .message(jsonParseException.getMessage()).build())
            );
        } else if (cause instanceof MismatchedInputException) {
            MismatchedInputException mismatchedInputException = (MismatchedInputException) cause;
            invalidInputResponse = new InvalidInputResponse(
                    HttpStatus.BAD_REQUEST.value(),
                    BadRequestError.INVALID_INPUT.getMessage(),
                    BadRequestError.INVALID_INPUT.getName(),
                    Collections.singleton(FieldErrorResponse.builder()
                            .field(mismatchedInputException.getPath().stream()
                                    .map(JsonMappingException.Reference::getFieldName)
                                    .collect(Collectors.joining(".")))
                            .message(mismatchedInputException.getMessage()).build()));
        } else if (cause instanceof JsonMappingException) {
            JsonMappingException jsonMappingException = (JsonMappingException) cause;
            invalidInputResponse = new InvalidInputResponse(
                    HttpStatus.BAD_REQUEST.value(),
                    BadRequestError.INVALID_INPUT.getMessage(),
                    BadRequestError.INVALID_INPUT.getName(),
                    Collections.singleton(FieldErrorResponse.builder()
                            .field(jsonMappingException.getPath().stream()
                                    .map(JsonMappingException.Reference::getFieldName)
                                    .collect(Collectors.joining(".")))
                            .message(jsonMappingException.getMessage()).build()));
        } else {
            invalidInputResponse = new InvalidInputResponse(
                    HttpStatus.BAD_REQUEST.value(),
                    BadRequestError.INVALID_INPUT.getMessage(),
                    BadRequestError.INVALID_INPUT.getName(),
                    Collections.singleton(FieldErrorResponse.builder()
                            .message(cause.getMessage()).build()));
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(invalidInputResponse);
    }

    @ExceptionHandler(ResponseException.class)
    public ResponseEntity<ErrorResponse<Object>> handleResponseException(ResponseException e, HttpServletRequest request) {
        log.warn("Failed to handle request {}: {}", request.getRequestURI(), e.getError().getMessage(), e);
        ResponseError error = e.getError();
        String message = languageService.getMessage(error.getName(), e.getError().getMessage(), e.getParams());
        return ResponseEntity.status(error.getStatus())
                .body(ErrorResponse.<Object>builder()
                        .code(error.getCode())
                        .error(error.getName())
                        .message(message)
                        .data(e.getData())
                        .build());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<InvalidInputResponse> handleValidationException(MethodArgumentNotValidException e, HttpServletRequest request) {
        BindingResult bindingResult = e.getBindingResult();
        Set<FieldErrorResponse> fieldErrors = bindingResult.getAllErrors().stream()
                .map(objectError -> {
                    try {
                        FieldError fieldError = (FieldError) objectError;
                        String message = languageService.getMessage(
                                fieldError.getDefaultMessage(), fieldError.getDefaultMessage());
                        return FieldErrorResponse.builder()
                                .field(fieldError.getField())
                                .objectName(fieldError.getObjectName())
                                .message(message)
                                .build();
                    } catch (ClassCastException ex) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        log.warn("Failed to handle request " + request.getRequestURI() + ": " + e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new InvalidInputResponse(
                        HttpStatus.BAD_REQUEST.value(),
                        BadRequestError.INVALID_INPUT.getMessage(),
                        BadRequestError.INVALID_INPUT.getName(),
                        fieldErrors));
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<ErrorResponse<Void>> handleResponseException(Exception e, HttpServletRequest request) {
//        e.
        ResponseError error = InternalServerError.INTERNAL_SERVER_ERROR;
        log.error("Failed to handle request " + request.getRequestURI() + ": " + error.getMessage(), e);
        languageService.getMessage(
                InternalServerError.INTERNAL_SERVER_ERROR.getName(), "There are somethings wrong: {0}", e);
        return ResponseEntity.status(error.getStatus())
                .body(ErrorResponse.<Void>builder()
                        .code(error.getCode())
                        .error(error.getName())
                        .message(e.getMessage())
                        .build());
    }

    @ExceptionHandler({DataIntegrityViolationException.class, NonTransientDataAccessException.class, DataAccessException.class})
    public ResponseEntity<ErrorResponse<Void>> handleDataAccessException(DataAccessException e, HttpServletRequest request) {
//        e.
        ResponseError error = InternalServerError.INTERNAL_SERVER_ERROR;
        log.error("Failed to handle request " + request.getRequestURI() + ": " + error.getMessage(), e);
        log.error(e.getMessage(), e);
        String msg = languageService.getMessage(
                InternalServerError.INTERNAL_SERVER_ERROR.getName(), "There are somethings wrong: {0}", e.getClass().getName());
        return ResponseEntity.status(error.getStatus())
                .body(ErrorResponse.<Void>builder()
                        .code(error.getCode())
                        .error(error.getName())
                        .message(msg)
                        .build());
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<InvalidInputResponse> handleValidationException(
            MissingServletRequestParameterException e, HttpServletRequest request) {
        log.warn("Failed to handle request " + request.getRequestURI() + ": " + e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new InvalidInputResponse(
                        HttpStatus.BAD_REQUEST.value(),
                        e.getMessage(),
                        BadRequestError.INVALID_INPUT.getName(),
                        Collections.singleton(FieldErrorResponse.builder()
                                .field(e.getParameterName())
                                .message(e.getMessage())
                                .build())));
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<InvalidInputResponse> handleValidationException(
            BindException e, HttpServletRequest request) {
        log.warn("Failed to handle request " + request.getRequestURI() + ": " + e.getMessage(), e);
        Set<FieldErrorResponse> fieldsErrors = e.getFieldErrors().stream()
                .map(fieldError -> FieldErrorResponse.builder()
                        .field(fieldError.getField())
                        .objectName(fieldError.getObjectName())
                        .message(fieldError.getDefaultMessage())
                        .build())
                .collect(Collectors.toSet());
        String message = fieldsErrors.stream()
                .map(FieldErrorResponse::getMessage)
                .collect(Collectors.joining(";"));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new InvalidInputResponse(
                        HttpStatus.BAD_REQUEST.value(),
                        message,
                        BadRequestError.INVALID_INPUT.name(),
                        fieldsErrors
                ));
    }

    @ExceptionHandler(MismatchedInputException.class)
    public ResponseEntity<InvalidInputResponse> handleValidationException(
            MismatchedInputException e, HttpServletRequest request) {
        log.warn("Failed to handle request " + request.getRequestURI() + ": " + e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new InvalidInputResponse(
                        HttpStatus.BAD_REQUEST.value(),
                        BadRequestError.INVALID_INPUT.getMessage(),
                        BadRequestError.INVALID_INPUT.getName(),
                        Collections.singleton(FieldErrorResponse.builder()
                                .message(e.getMessage())
                                .build())));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse<Void>> handleValidationException(
            AccessDeniedException e, HttpServletRequest request) {
        log.warn("Failed to handle request " + request.getRequestURI() + ": " + e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ErrorResponse.<Void>builder()
                        .error(AccessDeniedError.ACCESS_DENIED.getName())
                        .message("You were not permitted to request " + request.getMethod() + " " + request.getRequestURI())
                        .build());
    }
}
