package com.example.claimsystem.exception;

public class RequestIsInvalid extends RuntimeException {
    public RequestIsInvalid(String message) {
        super(message);
    }
}
