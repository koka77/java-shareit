package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Optional;

public interface BookingService {
    Optional<BookingDto> create(long userId, BookingDto dto);
}
