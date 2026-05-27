package com.zgamelogic.app.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ApplicationExceptionHandler {
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({InvalidDiscordCodeException.class, InvalidDiscordTokenException.class, InvalidMsmTokenException.class})
    public String handleInvalidDiscordCodeException(RuntimeException e) {
        return e.getMessage();
    }
}
