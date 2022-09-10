package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.BookingDtoState;

import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static ru.practicum.shareit.booking.BookingTestData.*;

public class BookingMapperTest {
    @Test
    void testToBookingDtoStatePast() {
        BookingDtoState bookingDtoStateNew = BookingMapper.toBookingDtoState(booking1);
        bookingDtoStateNew.getItem().setComments(new ArrayList<>());
        assertThat(bookingDtoStateNew, equalTo(bookingDtoState1));
    }

    @Test
    void testToBookingDtoStateWaitingCurrent() {
        BookingDtoState bookingDtoStateNew = BookingMapper.toBookingDtoState(booking4);
        bookingDtoStateNew.getItem().setComments(new ArrayList<>());
        assertThat(bookingDtoStateNew, equalTo(bookingDtoState4));
    }

}
