package com.earth.ureverse.global.common.response;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class CustomError {
    private final String message;
    private final int status;

    public CustomError(String message, HttpStatus status) {
        this.message = message;
        this.status = status.value();
    }
}
