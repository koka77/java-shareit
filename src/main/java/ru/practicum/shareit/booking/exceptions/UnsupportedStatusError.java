package ru.practicum.shareit.booking.exceptions;

import lombok.Getter;

@Getter
public class UnsupportedStatusError {
    private final String message;

    public UnsupportedStatusError(String message) {
        this.message = message;
    }
}
