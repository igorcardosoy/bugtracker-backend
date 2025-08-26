package br.com.ifsp.tsi.bugtrackerbackend.exception;

import org.springframework.http.HttpStatus;

public class MessageNotFoundException extends ExceptionWithHttpCode {
    public MessageNotFoundException() {
        super("Message not found.", HttpStatus.NOT_FOUND);
    }
}