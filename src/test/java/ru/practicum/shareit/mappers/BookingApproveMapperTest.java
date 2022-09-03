package ru.practicum.shareit.mappers;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.practicum.shareit.AbstractControllerTest;
import ru.practicum.shareit.booking.dto.BookingApproveDto;
import ru.practicum.shareit.booking.mapper.BookingApproveMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BookingApproveMapperTest extends AbstractControllerTest {

    @Autowired
    BookingApproveMapper bookingApproveMapper;

    @Test
    void shouldUpdateBookingFromDtoCorrectly() {
        Booking booking = new Booking();
        ItemDto itemDto = new ItemDto();
        BookingApproveDto bookingApproveDto = new BookingApproveDto();
        bookingApproveDto.setStatus(BookingStatus.WAITING);
        bookingApproveDto.setItem(itemDto);
        bookingApproveMapper.updateBookingFromDto(bookingApproveDto, booking);

        assertEquals(booking.getStatus(), BookingStatus.WAITING);
    }

    @Test
    void shouldConvertToBookingCorrectly() {
        BookingApproveDto bookingApproveDto = new BookingApproveDto();
        bookingApproveDto.setItem(new ItemDto());
        bookingApproveDto.setStatus(BookingStatus.WAITING);
        Booking booking = bookingApproveMapper.toBooking(bookingApproveDto);

        assertEquals(booking.getStatus(), BookingStatus.WAITING);
    }


    @Test
    void shouldConvertToDtoCorrectly() {

        Booking booking = new Booking();
        booking.setItem(new Item());

        booking.setStatus(BookingStatus.WAITING);
        BookingApproveDto bookingApproveDto = bookingApproveMapper.toBookingDto(booking);

        assertEquals(bookingApproveDto.getStatus(), BookingStatus.WAITING);
    }
}
