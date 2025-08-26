package br.com.ifsp.tsi.bugtrackerbackend.exception;

import org.springframework.http.HttpStatus;

public class CodeException extends ExceptionWithHttpCode {
    public CodeException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }
}