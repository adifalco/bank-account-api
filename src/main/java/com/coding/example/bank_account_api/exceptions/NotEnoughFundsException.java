package com.coding.example.bank_account_api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class NotEnoughFundsException extends Exception {

    public NotEnoughFundsException(String message) {
        super(message);
    }
}
