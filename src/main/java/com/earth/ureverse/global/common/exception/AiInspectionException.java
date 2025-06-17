package com.earth.ureverse.global.common.exception;

public class AiInspectionException extends RuntimeException {
    public AiInspectionException() {
        super("AI 검사 중 오류가 발생했습니다.");
    }

    public AiInspectionException(String message) {
        super(message);
    }

    public AiInspectionException(String message, Throwable cause) {
        super(message, cause);
    }
}
