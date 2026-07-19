package me.melkx.authservice.exception;

public class FounderInvalidPasswordException extends RuntimeException {
    public FounderInvalidPasswordException() {
        super("Invalid founder password");
    }
}
