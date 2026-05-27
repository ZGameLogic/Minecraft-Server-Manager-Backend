package com.zgamelogic.app.exceptions;

public class InvalidDiscordTokenException extends RuntimeException {
    public InvalidDiscordTokenException() {
        super("Invalid discord auth token");
    }
}
