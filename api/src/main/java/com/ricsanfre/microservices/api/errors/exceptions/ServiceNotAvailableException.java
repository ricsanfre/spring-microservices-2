package com.ricsanfre.microservices.api.errors.exceptions;

public class ServiceNotAvailableException extends RuntimeException {

    public ServiceNotAvailableException(String message) {
        super(message);
    }

    public ServiceNotAvailableException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceNotAvailableException(Throwable cause) {
        super(cause);
    }
}
