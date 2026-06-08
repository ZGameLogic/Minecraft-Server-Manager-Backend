package com.zgamelogic.app.exceptions;

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException() {
        super("Unauthorized");
    }
}
