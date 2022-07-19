package ru.practicum.shareit.item.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.practicum.shareit.requests.ItemRequest;
import ru.practicum.shareit.user.User;

/**
 * // TODO .
 */

@Data
public class ItemDto {
    private Long id;
    private String name;
    private String description;
    private boolean available;
    private User owner;
    private ItemRequest request;
}
