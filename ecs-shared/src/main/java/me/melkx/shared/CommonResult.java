package me.melkx.shared;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CommonResult<T> {
    private String message;
    private int status;
    private T data;

    public static FeignException mapToFeignException(CommonResult<?> result) {
        return new FeignException(result.message, result.status);
    }

    public boolean isSuccess() {
        return this.status == 200 || this.status == 201 ||
                this.status == 202 || this.status == 204;
    }
}
