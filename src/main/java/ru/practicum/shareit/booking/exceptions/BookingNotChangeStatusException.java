package ru.practicum.shareit.booking.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BookingNotChangeStatusException extends RuntimeException {
    public BookingNotChangeStatusException(String s) {
        super(s);
    }
}
