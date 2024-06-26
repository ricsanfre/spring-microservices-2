package com.ricsanfre.microservices.util.http;

import com.ricsanfre.microservices.api.errors.ApiErrorResponse;
import com.ricsanfre.microservices.api.errors.exceptions.InvalidInputException;
import com.ricsanfre.microservices.api.errors.exceptions.NotFoundException;
import com.ricsanfre.microservices.api.errors.exceptions.ServiceNotAvailableException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.bind.MissingServletRequestParameterException;

import java.time.ZonedDateTime;

@ControllerAdvice
public class GlobalControllerExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(GlobalControllerExceptionHandler.class);

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = NotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFoundException(
            NotFoundException e,
            HttpServletRequest request) {
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(
                ZonedDateTime.now().toOffsetDateTime().toString(),
                request.getRequestURI(),
                HttpStatus.NOT_FOUND,
                e.getMessage()

        );
        return new ResponseEntity<>(apiErrorResponse, HttpStatus.NOT_FOUND);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = InvalidInputException.class)
    public ResponseEntity<Object> handleInvalidInputException(
            InvalidInputException e,
            HttpServletRequest request) {
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(
                ZonedDateTime.now().toOffsetDateTime().toString(),
                request.getRequestURI(),
                HttpStatus.BAD_REQUEST,
                e.getMessage()

        );
        return new ResponseEntity<>(apiErrorResponse, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(
            value = { MethodArgumentTypeMismatchException.class,
                    MissingServletRequestParameterException.class}
    )
    public ResponseEntity<Object> handleMethodArgumentTypeMismatch(
            Exception e,
            HttpServletRequest request) {
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(
                ZonedDateTime.now().toOffsetDateTime().toString(),
                request.getRequestURI(),
                HttpStatus.BAD_REQUEST,
                e.getMessage()

        );
        return new ResponseEntity<>(apiErrorResponse, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    @ExceptionHandler( value = ServiceNotAvailableException.class )
    public ResponseEntity<Object> handleServiceNotAvailable(
            ServiceNotAvailableException e,
            HttpServletRequest request) {
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(
                ZonedDateTime.now().toOffsetDateTime().toString(),
                request.getRequestURI(),
                HttpStatus.SERVICE_UNAVAILABLE,
                e.getMessage()

        );
        return new ResponseEntity<>(apiErrorResponse, HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(value = Exception.class)
    public final ResponseEntity<Object> handleException(
            Exception e,
            HttpServletRequest request) {
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(
                ZonedDateTime.now().toOffsetDateTime().toString(),
                request.getRequestURI(),
                HttpStatus.INTERNAL_SERVER_ERROR,
                e.getClass() + ": " + e.getMessage()

        );
        return new ResponseEntity<>(apiErrorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
