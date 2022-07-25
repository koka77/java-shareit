package ru.practicum.shareit.booking.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.Optional;

@Service
public class BookingServiceImpl implements BookingService {
    @Override
    public BookingDto create(long userId, BookingDto dto) {
        throw new UnsupportedOperationException("Метод еще не реализован");
    }
}
