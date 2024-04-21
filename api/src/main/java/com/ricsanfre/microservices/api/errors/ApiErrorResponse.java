package com.ricsanfre.microservices.api.errors;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

/*
  ApiErrorResponse: customized response

 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiErrorResponse {
    private String timestamp;
    private String path;
    private HttpStatus httpStatus;
    private String message;

}
