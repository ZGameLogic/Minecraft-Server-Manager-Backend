package com.zgamelogic.app.exceptions;

public class InvalidMsmTokenException extends RuntimeException {
    public InvalidMsmTokenException() {
        super("Invalid msm token");
    }
}
