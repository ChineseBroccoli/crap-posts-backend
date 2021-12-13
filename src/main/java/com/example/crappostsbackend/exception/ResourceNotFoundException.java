package com.example.crappostsbackend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends Exception {

    public ResourceNotFoundException(String message) {
       super(message);
    }

    public static ResourceNotFoundException postNotFoundById(Long postId) {
        return new ResourceNotFoundException("Post not found or deleted for this id :: " + postId);
    }

    public static ResourceNotFoundException userNotFoundById(Long userId) {
        return new ResourceNotFoundException("User not found or deleted for this id :: " + userId);
    }

    public static ResourceNotFoundException userNotOwnPost(Long postId) {
        return new ResourceNotFoundException("User does not own post with id :: " + postId);
    }

}
