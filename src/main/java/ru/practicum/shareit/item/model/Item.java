package ru.practicum.shareit.item.model;

import lombok.Data;
import lombok.NonNull;
import ru.practicum.shareit.requests.ItemRequest;
import ru.practicum.shareit.user.User;

import javax.validation.constraints.NotEmpty;

/**
 * // TODO .
 */

@Data
public class Item {
    private Long id;
    private String name;
    private String description;

    @NonNull
    private Boolean available;
    private User owner;
    private ItemRequest request;
}
