package com.web.chat.exceptions.enter;

public class IncorrectPasswordException extends EnterException {
    @Override
    public String getMessage() {
        return "Incorrect password";
    }
}
