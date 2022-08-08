package ru.practicum.shareit.booking.exceptions;

public class BookingNotChangeStatusException extends RuntimeException {
    public BookingNotChangeStatusException(String s) {
        super(s);
    }
}
