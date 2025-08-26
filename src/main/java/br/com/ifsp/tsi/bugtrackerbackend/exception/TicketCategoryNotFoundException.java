package br.com.ifsp.tsi.bugtrackerbackend.exception;

import org.springframework.http.HttpStatus;

public class TicketCategoryNotFoundException extends ExceptionWithHttpCode {
    public TicketCategoryNotFoundException() {
        super("Ticket category not found.", HttpStatus.NOT_FOUND);
    }
}