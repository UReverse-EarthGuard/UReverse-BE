package com.earth.ureverse.global.common.controller;

import com.earth.ureverse.global.common.exception.*;
import com.earth.ureverse.global.common.response.CommonResponseEntity;
import com.earth.ureverse.global.common.response.CustomError;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CommonControllerAdvice {
    private ResponseEntity<CommonResponseEntity<Object>> response(
            Throwable throwable, HttpStatus status) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<>(
                new CommonResponseEntity<>(
                        false, null, new CustomError(throwable.getMessage(), status)),
                headers,
                status);
    }

    @ExceptionHandler({IllegalParameterException.class, BadRequestException.class})
    public ResponseEntity<?> handleBadRequestException(Exception e) {
        return response(e, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> handleExceptionForBadRequest(Exception e) {
        return response(e, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGlobalException(Exception e) {
        return response(e, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<?> handleAuthenticationException(AuthenticationException e) {
        return response(e, HttpStatus.UNAUTHORIZED);    // 401
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<?> handleTokenExpiredException(TokenExpiredException e) {
        return response(e, HttpStatus.UNAUTHORIZED);    // 401
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CommonResponseEntity<Object>> handleValidationException(MethodArgumentNotValidException e) {
        String message = e.getBindingResult()
                .getAllErrors()
                .stream()
                .findFirst()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .orElse("잘못된 요청입니다.");

        return ResponseEntity
                .badRequest()
                .body(CommonResponseEntity.error(HttpStatus.BAD_REQUEST, message));
    }

    @ExceptionHandler(InactiveUserException.class)
    public ResponseEntity<CommonResponseEntity<?>> handleInactiveUserException(InactiveUserException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(CommonResponseEntity.error(HttpStatus.UNAUTHORIZED, e.getMessage()));
    }

    @ExceptionHandler(AlreadyWithdrawnException.class)
    public ResponseEntity<CommonResponseEntity<?>> handleAlreadyWithdrawn(AlreadyWithdrawnException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(CommonResponseEntity.error(HttpStatus.FORBIDDEN, ex.getMessage()));
    }

    @ExceptionHandler(PasswordMismatchException.class)
    public ResponseEntity<CommonResponseEntity<?>> handlePasswordMismatch(PasswordMismatchException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(CommonResponseEntity.error(HttpStatus.UNAUTHORIZED, ex.getMessage()));
    }

}
