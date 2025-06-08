package com.earth.ureverse.global.common.exception;

import org.apache.logging.log4j.message.ParameterizedMessage;

public class CustomRuntimeException extends RuntimeException {
    public CustomRuntimeException(String message, Object... args) {
        super(ParameterizedMessage.format(message, args));
    }
}
