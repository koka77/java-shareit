package ru.practicum.shareit.booking.dto;

import lombok.Data;
import ru.practicum.shareit.booking.model.BookingStatus;

import javax.validation.constraints.Future;
import java.time.LocalDateTime;

@Data
public class BookingDto {

    private Long id;

    private Long itemId;

    private Long bookerId;

    @Future
    private LocalDateTime start;

    @Future
    private LocalDateTime end;

    private BookingStatus status;

}
