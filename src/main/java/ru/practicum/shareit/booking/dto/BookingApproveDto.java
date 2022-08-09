package ru.practicum.shareit.booking.dto;

import lombok.Data;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.constraints.Future;
import java.time.LocalDateTime;

@Data
public class BookingApproveDto {

    private Long id;

    private Long itemId;

    @Future
    private LocalDateTime start;

    @Future
    private LocalDateTime end;

    private BookingStatus status;

    private UserDto booker;

    private ItemDto item;


}
