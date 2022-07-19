package ru.practicum.shareit.booking;

import lombok.Data;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

/**
 * // TODO .
 */

@Data
public class Booking {
    private Long  id;
    private LocalDateTime star;
    private LocalDateTime end;
    private Item item;
    private User booker;
    private BookingStatus status;
}
