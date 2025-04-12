package com.payway.exceptions;

import com.payway.models.Unauthorized401Response;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Unauthorized401Response handleUnauthorizedException(UnauthorizedException ex) {
        return new Unauthorized401Response("failed", ex.getMessage());
    }

}

