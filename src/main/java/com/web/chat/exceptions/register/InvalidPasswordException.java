package com.web.chat.exceptions.register;

public class InvalidPasswordException extends RegisterException {
    public InvalidPasswordException(String message) {
        super(message);
    }
}
