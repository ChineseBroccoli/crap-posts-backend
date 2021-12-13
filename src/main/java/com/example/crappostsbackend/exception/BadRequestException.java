package com.example.crappostsbackend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class BadRequestException extends Exception {
    public BadRequestException(String message) {
        super(message);
    }

    public static BadRequestException alreadyUpvoted(Long postId) {
        return new BadRequestException("User already upvoted post with id :: " + postId);
    }

    public static BadRequestException alreadyDownvoted(Long postId) {
        return new BadRequestException("User already downvoted post with id :: " + postId);
    }
}
