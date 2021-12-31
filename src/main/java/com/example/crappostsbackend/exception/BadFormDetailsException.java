package com.example.crappostsbackend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Map;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class BadFormDetailsException extends Exception {
    private Map<String, String> errorMessages;
    public BadFormDetailsException(Map<String,String> errorMessages) {
        this.errorMessages = errorMessages;
    }

    public Map<String, String> getErrorMessages() {
        return errorMessages;
    }
}
