package ru.practicum.shareit.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.exception.*;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ControllerAdvice {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({UserNotFoundException.class,
            ItemRequestNotFoundException.class, ItemNotFoundException.class, UserIsNotOwnerException.class,
            BookingNotFoundException.class})
    public ErrorResponse handleNotFoundException(final Exception e) {
        return new ErrorResponse(
                String.format("\"%s\"", e.getMessage())
        );
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({NullEmailException.class, ItemRequestNotGoodParametrsException.class,
            ItemNotAvalibleException.class, UserIsNotBookerException.class, BookingDtoBadStateException.class,
            BookingNotChangeStatusException.class})
    public ErrorResponse handleBadRequestException(final Exception e) {
        return new ErrorResponse(
                String.format("\"%s\"", e.getMessage())
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Map<String, String>> errorHandler(IllegalArgumentException ex) {
        Map<String, String> resp = new HashMap<>();
        resp.put("error", String.format("Unknown state: UNSUPPORTED_STATUS"));
        return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
    }
}

