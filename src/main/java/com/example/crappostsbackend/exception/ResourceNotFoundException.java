package com.example.crappostsbackend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends Exception {

    private Map<String, String> errorMessages;

    public ResourceNotFoundException(Map<String, String> errorMessages) {
        this.errorMessages = errorMessages;
    }

    public Map<String, String> getErrorMessages() {
        return errorMessages;
    }

    public static ResourceNotFoundException postNotFoundById(Long postId) {
        Map<String, String> errorMessages = new HashMap<>();
        errorMessages.put("post", "Post not found or deleted with id :: " + postId);
        return new ResourceNotFoundException(errorMessages);
    }

    public static ResourceNotFoundException userNotFoundById(Long userId) {
        Map<String, String> errorMessages = new HashMap<>();
        errorMessages.put("user", "User not found or deleted with id :: " + userId);
        return new ResourceNotFoundException(errorMessages);
    }

    public static ResourceNotFoundException userNotOwnPost(Long postId) {
        Map<String, String> errorMessages = new HashMap<>();
        errorMessages.put("post", "User does not own post with id :: " + postId);
        return new ResourceNotFoundException(errorMessages);
    }

}
