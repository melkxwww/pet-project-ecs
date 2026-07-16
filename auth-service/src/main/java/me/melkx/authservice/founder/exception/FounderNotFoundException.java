package me.melkx.authservice.founder.exception;

public class FounderNotFoundException extends RuntimeException {
    public FounderNotFoundException() {
        super("Founder not found");
    }
}
