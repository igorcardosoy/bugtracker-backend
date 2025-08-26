package br.com.ifsp.tsi.bugtrackerbackend.exception;

import org.springframework.http.HttpStatus;

public class TokenException extends ExceptionWithHttpCode {
    public TokenException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }
}