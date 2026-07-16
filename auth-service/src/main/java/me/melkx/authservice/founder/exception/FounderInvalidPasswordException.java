package me.melkx.authservice.founder.exception;

public class FounderInvalidPasswordException extends RuntimeException {
    public FounderInvalidPasswordException(String message) {
        super("Invalid founder password");
    }
}
