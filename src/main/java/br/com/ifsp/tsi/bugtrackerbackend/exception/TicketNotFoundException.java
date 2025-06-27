package br.com.ifsp.tsi.bugtrackerbackend.exception;

import org.springframework.http.HttpStatus;

public class TicketNotFoundException extends ExceptionWithHttpCode {
    public TicketNotFoundException() {
        super("Ticket not found.", HttpStatus.NOT_FOUND);
    }
}