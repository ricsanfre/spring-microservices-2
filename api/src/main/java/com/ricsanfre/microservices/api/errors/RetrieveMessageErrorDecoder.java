package com.ricsanfre.microservices.api.errors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ricsanfre.microservices.api.errors.exceptions.InvalidInputException;
import com.ricsanfre.microservices.api.errors.exceptions.NotFoundException;
import feign.Response;
import feign.codec.ErrorDecoder;

import java.io.IOException;
import java.io.InputStream;

public class RetrieveMessageErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder errorDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {

        ApiErrorResponse message = null;
        try (InputStream bodyIs = response.body().asInputStream()) {
            ObjectMapper mapper = new ObjectMapper();
            message = mapper.readValue(bodyIs, ApiErrorResponse.class);
        } catch (IOException e) {
            return new Exception(e.getMessage());
        }
        switch (response.status()) {
            // NOT_FOUND
            case 404:
                return new NotFoundException(message.message() != null ? message.message() : "Not found");
            // BAD_REQUEST
            case 400:
                return new InvalidInputException(message.message() != null ? message.message() : "Bad Request");
            default:
                return errorDecoder.decode(methodKey, response);
        }
    }
}
