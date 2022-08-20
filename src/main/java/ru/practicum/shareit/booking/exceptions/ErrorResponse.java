package ru.practicum.shareit.booking.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ErrorResponse {
    public final HttpStatus status;
    public final String error;

    public ErrorResponse(HttpStatus status, String error) {
        this.status = status;

        this.error = error;
    }

}
