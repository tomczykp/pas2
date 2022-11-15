package com.example.demo.app.exceptions;

import java.time.DateTimeException;

public class DateException extends DateTimeException {

    public DateException() {
        super("Invalid date");
    }

}
