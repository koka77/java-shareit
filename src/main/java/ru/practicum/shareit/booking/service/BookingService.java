package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingApproveDto;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

public interface BookingService {
    BookingDto create(Long userId, BookingDto dto);

    BookingApproveDto getBookingById(Long userId, Long bookingId);

    BookingApproveDto approveBooking(Long userId, Long bookingId, boolean approved);

    List<BookingApproveDto> getBookingByCurrentOwner(long userId, String state);

    List<BookingApproveDto> getBookingByCurrentUser(long userId, String state);


}
