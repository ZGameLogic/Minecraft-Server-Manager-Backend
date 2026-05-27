package com.zgamelogic.app.exceptions;

public class InvalidDiscordCodeException extends RuntimeException {
    public InvalidDiscordCodeException() {
        super("Invalid discord auth code");
    }
}
