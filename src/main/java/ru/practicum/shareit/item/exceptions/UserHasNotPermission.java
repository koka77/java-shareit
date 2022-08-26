package ru.practicum.shareit.item.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserHasNotPermission extends RuntimeException {
    public UserHasNotPermission() {
        super();
    }

    public UserHasNotPermission(String message) {
        super(message);
    }
}
