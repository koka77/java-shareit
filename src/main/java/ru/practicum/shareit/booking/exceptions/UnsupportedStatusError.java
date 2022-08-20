package ru.practicum.shareit.booking.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class UnsupportedStatusError {
    private final String message;

    public UnsupportedStatusError(String message) {
        this.message = message;
    }
}
