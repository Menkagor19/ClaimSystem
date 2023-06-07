package com.example.claimsystem.handler;

import com.example.claimsystem.exception.RequestIsInvalid;
import com.example.claimsystem.exception.ResponseIsNullorNotCastableException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResponseIsNullorNotCastableException.class)
    public ResponseEntity<String> handleException(ResponseIsNullorNotCastableException ex){
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(RequestIsInvalid.class)
    public ResponseEntity<String> handle(RequestIsInvalid ex){
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
