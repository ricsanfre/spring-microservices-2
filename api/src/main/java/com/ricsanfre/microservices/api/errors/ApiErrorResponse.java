package com.ricsanfre.microservices.api.errors;

import org.springframework.http.HttpStatus;

/*
  ApiErrorResponse: customized response

 */

public record ApiErrorResponse (
        String timestamp,
        String path,
        HttpStatus httpStatus,
        String message ) {

}
