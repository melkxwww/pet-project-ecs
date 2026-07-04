package me.melkx.shared;

import lombok.Getter;

@Getter
public class FeignException extends RuntimeException {
    private final int status;

    public FeignException(String message, int status) {
        super(message);
        this.status = status;
    }
}
