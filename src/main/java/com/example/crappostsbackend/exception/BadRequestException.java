package com.example.crappostsbackend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class BadRequestException extends Exception {

    private Map<String, String> errorMessages;

    public BadRequestException(Map<String, String> errorMessages) {
        this.errorMessages = errorMessages;
    }

    public Map<String, String> getErrorMessages() {
        return errorMessages;
    }

    public static BadRequestException alreadyUpvoted(Long postId) {
        Map<String, String> errorMessages = new HashMap<>();
        errorMessages.put("post", "already upvoted post with id :: " + postId);
        return new BadRequestException(errorMessages);
    }

    public static BadRequestException alreadyDownvoted(Long postId) {
        Map<String, String> errorMessages = new HashMap<>();
        errorMessages.put("post", "already downvoted post with id :: " + postId);
        return new BadRequestException(errorMessages);
    }
}
