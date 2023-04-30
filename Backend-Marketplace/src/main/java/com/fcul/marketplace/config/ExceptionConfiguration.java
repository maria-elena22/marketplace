package com.fcul.marketplace.config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fcul.marketplace.exceptions.*;
import lombok.Data;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.Serializable;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@ControllerAdvice
public class ExceptionConfiguration extends ResponseEntityExceptionHandler {

    private static final Class[] NOT_FOUND_EXCEPTIONS = {
    };

    private static final Class[] CONFLICT_EXCEPTIONS = {
            SignUpException.class,
            UniProdNotOwnedByFornecedor.class,
            ForbiddenActionException.class,
            PaymentFailedException.class,
            EncomendaAlreadyCancelledException.class,
            EncomendaCannotBeCancelledException.class
    };

    private static final Class[] BAD_REQUEST_EXCEPTIONS = {
    };

    private static final Class[] FORBIDEN_EXCEPTION = {
            JWTTokenMissingException.class,
            BadCredentialsException.class,
            ForbiddenActionException.class,
            InactiveAccountException.class
    };


    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleAll(Exception exception, WebRequest request) {
        HttpHeaders httpHeaders = new HttpHeaders();

        List<Class> notFound = Arrays.asList(NOT_FOUND_EXCEPTIONS);
        List<Class> conflict = Arrays.asList(CONFLICT_EXCEPTIONS);
        List<Class> badRequest = Arrays.asList(BAD_REQUEST_EXCEPTIONS);
        List<Class> forbidden = Arrays.asList(FORBIDEN_EXCEPTION);


        HttpStatus status;
        if (notFound.contains(exception.getClass())) {
            status = HttpStatus.NOT_FOUND;
        } else if (conflict.contains(exception.getClass())) {
            status = HttpStatus.CONFLICT;
        } else if (badRequest.contains(exception.getClass())) {
            status = HttpStatus.BAD_REQUEST;
        } else if (forbidden.contains(exception.getClass())) {
            status = HttpStatus.FORBIDDEN;
        } else {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        ErrorResponseBody error = new ErrorResponseBody();
        error.setException(exception.getClass().getSimpleName());
        error.setMessage(exception.getMessage());
        error.setErrorCode(status.value());
        error.setPath(getContextPath(request));

        return new ResponseEntity<>(error, new HttpHeaders(), error.getErrorCode());


    }

    private String getContextPath(WebRequest request) {
        return Optional.ofNullable(request)
                .filter(ServletWebRequest.class::isInstance)
                .map(ServletWebRequest.class::cast)
                .map(swr -> swr.getRequest().getRequestURI())
                .orElse(null);
    }


    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    private static class ErrorResponseBody implements Serializable {

        private ZonedDateTime timestamp = Instant.now().atZone(ZoneId.of("UTC"));
        private String exception;
        private Integer errorCode;
        private String path;
        private String message;

    }

}
