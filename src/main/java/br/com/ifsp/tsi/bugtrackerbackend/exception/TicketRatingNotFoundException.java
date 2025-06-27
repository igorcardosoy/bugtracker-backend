package br.com.ifsp.tsi.bugtrackerbackend.exception;

import org.springframework.http.HttpStatus;

public class TicketRatingNotFoundException extends ExceptionWithHttpCode {
    public TicketRatingNotFoundException() {
        super("Ticket category not found.", HttpStatus.NOT_FOUND);
    }
}