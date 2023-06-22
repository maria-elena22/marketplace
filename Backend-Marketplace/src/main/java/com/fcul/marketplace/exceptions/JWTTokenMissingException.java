package com.fcul.marketplace.exceptions;

public class JWTTokenMissingException extends Exception {

    public JWTTokenMissingException(String message) {
        super(message);
    }
}
