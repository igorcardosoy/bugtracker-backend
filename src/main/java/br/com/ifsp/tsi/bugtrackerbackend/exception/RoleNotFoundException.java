package br.com.ifsp.tsi.bugtrackerbackend.exception;

import org.springframework.http.HttpStatus;

public class RoleNotFoundException extends ExceptionWithHttpCode {
    public RoleNotFoundException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }
}