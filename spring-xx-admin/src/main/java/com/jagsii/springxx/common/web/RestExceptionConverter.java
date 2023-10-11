package com.jagsii.springxx.common.web;

import com.jagsii.springxx.common.utils.ClientException;
import com.jagsii.springxx.common.utils.ServerException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;

/**
 * Convert the exception to the R&lt;Void&gt; object.
 */
@Slf4j
public class RestExceptionConverter {
    public static R<Void> convert(RuntimeException ex) {
        if (ex instanceof AuthenticationException) {
            return convertAuthenticationException((AuthenticationException) ex, HttpStatus.UNAUTHORIZED);
        }
        if (ex instanceof AccessDeniedException) {
            return convert(ex, ex.getMessage(), HttpStatus.FORBIDDEN);
        }
        if (ex instanceof ClientException) {
            return convertClientException((ClientException) ex, HttpStatus.BAD_REQUEST);
        }
        if (ex instanceof ServerException) {
            return convertServerException((ServerException) ex, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return convert(ex, null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public static R<Void> convertAuthenticationException(
            AuthenticationException ex, HttpStatus status) {

        return convert(ex, ex.getMessage(), status);
    }

    public static R<Void> convertClientException(
            ClientException ex) {

        return convert(ex, ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    public static R<Void> convertClientException(
            ClientException ex, HttpStatus status) {

        return convert(ex, ex.getMessage(), status);
    }

    public static R<Void> convertServerException(
            ServerException ex) {

        return convert(ex, null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public static R<Void> convertServerException(
            ServerException ex, HttpStatus status) {
        log.error("server error occurred.", ex);
        return convert(ex, null, status);
    }

    public static R<Void> convert(Exception ex, String message, HttpStatus status) {
        if (!(ex instanceof RuntimeException)) {
            log.error("request failed", ex);
        }
        return R.failed(status.value(), message);
    }
}
