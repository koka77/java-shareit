package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.service.BookingService;

/**
 * // TODO .
 */
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {

    private final BookingService service;

    @Autowired
    public BookingController(BookingService service) {
        this.service = service;
    }

    public BookingDto create(@RequestHeader("X-Sharer-User-Id") long userId,
                             @RequestBody BookingDto bookingDto) {

        return service.create(userId, bookingDto);
    }
}