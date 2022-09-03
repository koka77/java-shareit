package ru.practicum.shareit.mappers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.practicum.shareit.AbstractControllerTest;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BookingMapperTest extends AbstractControllerTest {

    @Autowired
    BookingMapper bookingMapper;

    @Test
    void shouldUpdateBookingFromDtoCorrectly() {
        BookingDto dto = new BookingDto();
        dto.setStatus(BookingStatus.WAITING);
        Booking booking = new Booking();
        bookingMapper.updateBookingFromDto(dto, booking);

        assertEquals(booking.getStatus(), dto.getStatus());
    }
}
