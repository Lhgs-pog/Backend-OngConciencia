package com.backend.OngConciencia.Security;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/*
*  Exceção personalisada
* */
@ResponseStatus(HttpStatus.NOT_FOUND) // Define o status HTTP retornado
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}