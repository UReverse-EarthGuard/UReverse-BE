package com.earth.ureverse.global.common.exception;

public class AlreadyWithdrawnException extends RuntimeException {

    public AlreadyWithdrawnException() {
        super("이미 탈퇴한 사용자입니다.");
    }

}
