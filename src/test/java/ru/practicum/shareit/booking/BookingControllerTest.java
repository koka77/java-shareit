package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.AbstractControllerTest;
import ru.practicum.shareit.booking.dto.BookingApproveDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.service.UserService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@DirtiesContext
@Transactional
class BookingControllerTest extends AbstractControllerTest {
    @Autowired
    BookingController bookingController;

    @Autowired
    UserService userService;

    @Autowired
    ItemService itemService;

    @Autowired
    BookingService bookingService;

    @Test
    @DirtiesContext
    void testCreateShouldReturnOk() throws Exception {
        /*userService.create(userDto);

        userService.create(userDto2);
        itemService.create(1l, itemDto);
        bookingDto.setItemId(1l);
        LocalDateTime start = LocalDateTime.now().plusMinutes(1l);
        LocalDateTime end = LocalDateTime.now().plusMinutes(2l);

        bookingDto.setStart(start);
        bookingDto.setEnd(end);
        bookingDto.setStatus(BookingStatus.WAITING);*/

        createBooking();

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/bookings")
                                .content(objectToJson(bookingDto))
                                .characterEncoding(StandardCharsets.UTF_8)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("X-Sharer-User-Id", 2L)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DirtiesContext
    void testCreateShouldReturnException() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/bookings")
                                .content(objectToJson(bookingDto))
                                .characterEncoding(StandardCharsets.UTF_8)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("X-Sharer-User-Id", 1L)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DirtiesContext
    void approveStatus() throws Exception {
        createBooking();
        mockMvc.perform(
                        MockMvcRequestBuilders.patch("/bookings/1?approved=true")
                                .characterEncoding(StandardCharsets.UTF_8)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("X-Sharer-User-Id", 1L)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DirtiesContext
    void getBookingById() throws Exception {
        createBooking();
        BookingApproveDto dto = bookingService.getBookingById(1l, 1l);
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/bookings/1")
                                .characterEncoding(StandardCharsets.UTF_8)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("X-Sharer-User-Id", 1L)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(dto)));
    }

    @Test
    @DirtiesContext
    void getBookingByIdShouldReturnNotFoundException() throws Exception {

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/bookings/1")
                                .characterEncoding(StandardCharsets.UTF_8)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("X-Sharer-User-Id", 2L)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DirtiesContext
    void getBookingCurrentUser() throws Exception {
        createBooking();

        List<BookingApproveDto> dtoList = bookingService
                .getBookingByCurrentUser(1l, "ALL", 1, 20);

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/bookings?state=ALL")
                                .characterEncoding(StandardCharsets.UTF_8)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("X-Sharer-User-Id", 1L)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(dtoList)));
    }

    @Test
    @DirtiesContext
    void getBookingCurrentUserShouldReturnException() throws Exception {
        createBooking();

        List<BookingApproveDto> dtoList = bookingService
                .getBookingByCurrentUser(1l, "ALL", 1, 20);

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/bookings?state=ALL")
                                .characterEncoding(StandardCharsets.UTF_8)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("X-Sharer-User-Id", 100L)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DirtiesContext
    void getBookingCurrentOwner() throws Exception {
        createBooking();

        List<BookingApproveDto> dtoList = bookingService
                .getBookingByCurrentOwner(1l, "ALL", 1, 20);

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/bookings/owner?state=ALL")
                                .characterEncoding(StandardCharsets.UTF_8)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("X-Sharer-User-Id", 1L)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(dtoList)));
    }

    private void createBooking() {
        userService.create(userDto);
        userService.create(userDto2);
        userService.create(userDto3);


        itemService.create(1l, itemDto);
        itemService.create(2l, itemDto2);

        bookingDto.setItemId(1l);
        bookingDto2.setItemId(2l);


        LocalDateTime start = LocalDateTime.now().plusMinutes(1l);
        LocalDateTime end = LocalDateTime.now().plusMinutes(2l);

        bookingDto.setStart(start);
        bookingDto.setEnd(end);
        bookingDto.setStatus(BookingStatus.WAITING);

        bookingDto2.setStart(start.plusMinutes(1));
        bookingDto2.setEnd(end.plusMinutes(1));
        bookingDto2.setStatus(BookingStatus.WAITING);

        bookingService.create(2l, bookingDto);
        bookingService.create(1l, bookingDto2);
    }
}