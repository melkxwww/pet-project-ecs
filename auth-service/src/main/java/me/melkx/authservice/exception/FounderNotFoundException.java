package me.melkx.authservice.exception;

public class FounderNotFoundException extends RuntimeException {
    public FounderNotFoundException() {
        super("Founder not found");
    }
}
