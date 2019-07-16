package com.example.demo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class AccountDoesNotExist extends RuntimeException {

    public AccountDoesNotExist(String message) {
        super(message);
    }
}
