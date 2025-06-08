package com.earth.ureverse.global.common.exception;

public class NotFoundException extends CustomRuntimeException {
    public NotFoundException(String message, Object... args) {
        super(message, args);
    }
}