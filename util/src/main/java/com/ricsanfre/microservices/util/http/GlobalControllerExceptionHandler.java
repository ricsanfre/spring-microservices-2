package com.ricsanfre.microservices.util.http;

import com.ricsanfre.microservices.api.errors.ApiErrorResponse;
import com.ricsanfre.microservices.api.errors.exceptions.NotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZonedDateTime;

@ControllerAdvice
public class GlobalControllerExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(GlobalControllerExceptionHandler.class);

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

    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(
            IllegalArgumentException e,
            HttpServletRequest request) {
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(
                ZonedDateTime.now().toOffsetDateTime().toString(),
                request.getRequestURI(),
                HttpStatus.BAD_REQUEST,
                e.getMessage()

        );
        return new ResponseEntity<>(apiErrorResponse, HttpStatus.BAD_REQUEST);
    }

}
