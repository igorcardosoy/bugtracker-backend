package br.com.ifsp.tsi.bugtrackerbackend.exception;

import org.springframework.http.HttpStatus;

public class ProfilePictureException extends ExceptionWithHttpCode {


    public ProfilePictureException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }
}
