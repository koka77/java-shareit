package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.AbstractControllerTest;
import ru.practicum.shareit.booking.dto.BookingApproveDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.service.UserService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
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

    @Autowired
    BookingRepository bookingRepository;

    @Test
    @DirtiesContext
    void createShouldReturnItemNotAvailableExceptionException() throws Exception {

        createBooking();

        Booking booking = bookingRepository.findById(1l).get();
        booking.getItem().setAvailable(false);
        bookingRepository.save(booking);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/bookings")
                                .content(objectToJson(bookingDto))
                                .characterEncoding(StandardCharsets.UTF_8)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("X-Sharer-User-Id", 2L)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DirtiesContext
    void createShouldReturnOk() throws Exception {

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
    void createShouldReturnException() throws Exception {

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
    void createShouldReturnUserHasNotPermissionException() throws Exception {
        createBooking();
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
    void approveStatusIsOk() throws Exception {

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
    void approveStatusReject() throws Exception {

        createBooking();

        mockMvc.perform(
                        MockMvcRequestBuilders.patch("/bookings/1?approved=false")
                                .characterEncoding(StandardCharsets.UTF_8)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("X-Sharer-User-Id", 1L)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }


    @Test
    @DirtiesContext
    void approveStatusShouldReturnException() throws Exception {

        createBooking();

        mockMvc.perform(
                        MockMvcRequestBuilders.patch("/bookings/1?approved=false")
                                .characterEncoding(StandardCharsets.UTF_8)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("X-Sharer-User-Id", 3L)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
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
    void getBookingByIdShouldReturnUserHasNotPermissionException() throws Exception {
        createBooking();
        BookingApproveDto dto = bookingService.getBookingById(1l, 1l);
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/bookings/1")
                                .characterEncoding(StandardCharsets.UTF_8)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("X-Sharer-User-Id", 3L)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
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
    void getBookingCurrentUserFuture() throws Exception {
        createBooking();

        List<BookingApproveDto> dtoList = bookingService
                .getBookingByCurrentUser(1l, "FUTURE", 1, 20);

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/bookings?state=FUTURE")
                                .characterEncoding(StandardCharsets.UTF_8)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("X-Sharer-User-Id", 1L)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(dtoList)));
    }

    @Test
    @DirtiesContext
    void getBookingCurrentUserCurrent() throws Exception {
        createBooking();

        List<BookingApproveDto> dtoList = bookingService
                .getBookingByCurrentUser(1l, "CURRENT", 1, 20);

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/bookings?state=CURRENT")
                                .characterEncoding(StandardCharsets.UTF_8)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("X-Sharer-User-Id", 1L)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(dtoList)));
    }

    @Test
    @DirtiesContext
    void getBookingCurrentUserPast() throws Exception {
        createBooking();

        List<BookingApproveDto> dtoList = bookingService
                .getBookingByCurrentUser(1l, "PAST", 1, 20);

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/bookings?state=PAST")
                                .characterEncoding(StandardCharsets.UTF_8)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("X-Sharer-User-Id", 1L)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(dtoList)));
    }

    @Test
    @DirtiesContext
    void getBookingCurrentUserRejected() throws Exception {
        createBooking();

        List<BookingApproveDto> dtoList = bookingService
                .getBookingByCurrentUser(1l, "REJECTED", 1, 20);

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/bookings?state=REJECTED")
                                .characterEncoding(StandardCharsets.UTF_8)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("X-Sharer-User-Id", 1L)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(dtoList)));
    }

    @Test
    @DirtiesContext
    void getBookingCurrentUserWaiting() throws Exception {
        createBooking();

        List<BookingApproveDto> dtoList = bookingService
                .getBookingByCurrentUser(1l, "WAITING", 1, 20);

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/bookings?state=WAITING")
                                .characterEncoding(StandardCharsets.UTF_8)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("X-Sharer-User-Id", 1L)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(dtoList)));
    }

    @Test
    @DirtiesContext
    void getBookingCurrentUserShouldReturnUnsupportedStatusExceptionException() throws Exception {
        createBooking();

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/bookings?state=ERROR")
                                .characterEncoding(StandardCharsets.UTF_8)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("X-Sharer-User-Id", 1L)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
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
    void getBookingCurrentOwnerAll() throws Exception {
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


    @Test
    @DirtiesContext
    void getBookingCurrentOwnerFuture() throws Exception {
        createBooking();

        List<BookingApproveDto> dtoList = bookingService
                .getBookingByCurrentOwner(1l, "FUTURE", 1, 20);

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/bookings/owner?state=FUTURE")
                                .characterEncoding(StandardCharsets.UTF_8)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("X-Sharer-User-Id", 1L)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(dtoList)));
    }


    @Test
    @DirtiesContext
    void getBookingCurrentOwnerCurrent() throws Exception {
        createBooking();

        List<BookingApproveDto> dtoList = bookingService
                .getBookingByCurrentOwner(1l, "CURRENT", 1, 20);

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/bookings/owner?state=CURRENT")
                                .characterEncoding(StandardCharsets.UTF_8)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("X-Sharer-User-Id", 1L)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(dtoList)));
    }


    @Test
    @DirtiesContext
    void getBookingCurrentOwnerPast() throws Exception {
        createBooking();

        List<BookingApproveDto> dtoList = bookingService
                .getBookingByCurrentOwner(1l, "PAST", 1, 20);

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/bookings/owner?state=PAST")
                                .characterEncoding(StandardCharsets.UTF_8)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("X-Sharer-User-Id", 1L)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(dtoList)));
    }

    @Test
    @DirtiesContext
    void getBookingCurrentOwnerApproved() throws Exception {
        createBooking();

        List<BookingApproveDto> dtoList = bookingService
                .getBookingByCurrentOwner(1l, "APPROVED", 1, 20);

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/bookings/owner?state=APPROVED")
                                .characterEncoding(StandardCharsets.UTF_8)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("X-Sharer-User-Id", 1L)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(dtoList)));
    }

    @Test
    @DirtiesContext
    void getBookingCurrentOwnerApprovedShouldReturnException() throws Exception {
        createBooking();

        Booking booking1 = bookingRepository.findById(1l).get();
        booking1.setStatus(BookingStatus.REJECTED);
        bookingRepository.save(booking1);
        Booking booking2 = bookingRepository.findById(1l).get();
        booking2.setStatus(BookingStatus.REJECTED);
        bookingRepository.save(booking2);

        List<BookingApproveDto> dtoList = bookingService
                .getBookingByCurrentOwner(1l, "APPROVED", 1, 20);

        mockMvc.perform(
                        MockMvcRequestBuilders.patch("/bookings/owner?approved=true")
                                .characterEncoding(StandardCharsets.UTF_8)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("X-Sharer-User-Id", 1L)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }


    @Test
    @DirtiesContext
    void getBookingCurrentOwnerShouldReturnUnsupportedStatusExceptionException()
            throws Exception {
        createBooking();

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/bookings/owner?approved=true&state=ERROR")
                                .characterEncoding(StandardCharsets.UTF_8)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("X-Sharer-User-Id", 1L)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
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