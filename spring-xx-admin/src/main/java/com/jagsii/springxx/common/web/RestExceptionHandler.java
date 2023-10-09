package com.jagsii.springxx.common.web;

import com.jagsii.springxx.common.utils.ClientException;
import com.jagsii.springxx.common.utils.ServerException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleOtherException(
            RuntimeException ex, WebRequest request) {
        HttpHeaders headers = new HttpHeaders();
        if (ex instanceof AuthenticationException) {
            return handleAuthenticationRequired((AuthenticationException) ex, headers, HttpStatus.UNAUTHORIZED, request);
        }
        if (ex instanceof ClientException) {
            return handleClientException((ClientException) ex, headers, HttpStatus.BAD_REQUEST, request);
        }
        if (ex instanceof ServerException) {
            return handleServerException((ServerException) ex, headers, HttpStatus.INTERNAL_SERVER_ERROR, request);
        }
        return handleExceptionInternal(ex, null,
                headers, HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    public ResponseEntity<Object> handleAuthenticationRequired(
            AuthenticationException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        return handleException(ex, ex.getMessage(), headers, status, request);
    }

    public ResponseEntity<Object> handleClientException(
            ClientException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        return handleException(ex, ex.getMessage(), headers, status, request);
    }

    public ResponseEntity<Object> handleServerException(
            ServerException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        return handleExceptionInternal(ex, null, headers, status, request);
    }

    @Override
    protected @NonNull ResponseEntity<Object> handleExceptionInternal(@NonNull Exception ex, @Nullable Object body, @NonNull HttpHeaders headers, @NonNull HttpStatus status, @NonNull WebRequest request) {
        return handleException(ex, null, headers, status, request);
    }

    private ResponseEntity<Object> handleException(Exception ex, String message, HttpHeaders headers, HttpStatus status, WebRequest request) {
        if (!(ex instanceof RuntimeException)) {
            log.error("request failed", ex);
        }
        return new ResponseEntity<>(R.failed(status.value(), message), headers, HttpStatus.OK);
    }
}
