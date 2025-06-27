package br.com.ifsp.tsi.bugtrackerbackend.exception;

import org.springframework.http.HttpStatus;

public class TicketCategoryNotFoundException extends ExceptionWithHttpCode {
    public TicketCategoryNotFoundException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }
}