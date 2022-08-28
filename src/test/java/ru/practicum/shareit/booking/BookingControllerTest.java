package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.AbstractControllerTest;
import ru.practicum.shareit.booking.dto.BookingApproveDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.assertThrows;


@DirtiesContext
@Transactional
class BookingControllerTest extends AbstractControllerTest {


    @Test
    @DirtiesContext
    void createShouldReturnItemNotAvailableExceptionException() throws Exception {

        try {
            createBooking();
        } catch (RuntimeException e) {
            e.printStackTrace();
        }

        Booking booking = bookingRepository.findById(1L).get();
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

        try {
            createBooking();
        } catch (RuntimeException e) {
            e.printStackTrace();
        }

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
        try {
            createBooking();
        } catch (RuntimeException e) {
            e.printStackTrace();
        }

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

        try {
            createBooking();
        } catch (RuntimeException e) {
            e.printStackTrace();
        }

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

        try {
            createBooking();
        } catch (RuntimeException e) {
            e.printStackTrace();
        }

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

        try {
            createBooking();
        } catch (RuntimeException e) {
            e.printStackTrace();
        }

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
    void approveStatusIsBad() throws Exception {

        try {
            createBooking();


            Booking booking = bookingRepository.findById(1L).get();
            booking.setStatus(BookingStatus.REJECTED);
            bookingRepository.save(booking);

            mockMvc.perform(
                            MockMvcRequestBuilders.patch("/bookings/1?approved=true")
                                    .characterEncoding(StandardCharsets.UTF_8)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .header("X-Sharer-User-Id", 1L)
                                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    @Test
    @DirtiesContext
    void getBookingById() throws Exception {
        try {
            createBooking();


            BookingApproveDto dto = bookingService.getBookingById(1L, 1L);
            mockMvc.perform(
                            MockMvcRequestBuilders.get("/bookings/1")
                                    .characterEncoding(StandardCharsets.UTF_8)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .header("X-Sharer-User-Id", 1L)
                                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().json(mapper.writeValueAsString(dto)));
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    @Test
    @DirtiesContext
    void getBookingByIdShouldReturnUserHasNotPermissionException() throws Exception {
        try {
            createBooking();

            BookingApproveDto dto = bookingService.getBookingById(1L, 1L);
            mockMvc.perform(
                            MockMvcRequestBuilders.get("/bookings/1")
                                    .characterEncoding(StandardCharsets.UTF_8)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .header("X-Sharer-User-Id", 3L)
                                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
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
        try {
            createBooking();
            List<BookingApproveDto> dtoList = bookingService
                    .getBookingByCurrentUser(1L, "ALL", 1, 20);

            mockMvc.perform(
                            MockMvcRequestBuilders.get("/bookings?state=ALL")
                                    .characterEncoding(StandardCharsets.UTF_8)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .header("X-Sharer-User-Id", 1L)
                                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().json(mapper.writeValueAsString(dtoList)));
        } catch (RuntimeException e) {
            e.printStackTrace();
        }

    }


    @Test
    @DirtiesContext
    void getBookingCurrentUserFuture() throws Exception {
        try {
            createBooking();

            List<BookingApproveDto> dtoList = bookingService
                    .getBookingByCurrentUser(1L, "FUTURE", 1, 20);

            mockMvc.perform(
                            MockMvcRequestBuilders.get("/bookings?state=FUTURE")
                                    .characterEncoding(StandardCharsets.UTF_8)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .header("X-Sharer-User-Id", 1L)
                                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().json(mapper.writeValueAsString(dtoList)));
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    @Test
    @DirtiesContext
    void getBookingCurrentUserCurrent() throws Exception {

        try {
            createBooking();

            List<BookingApproveDto> dtoList = bookingService
                    .getBookingByCurrentUser(1L, "CURRENT", 1, 20);

            mockMvc.perform(
                            MockMvcRequestBuilders.get("/bookings?state=CURRENT")
                                    .characterEncoding(StandardCharsets.UTF_8)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .header("X-Sharer-User-Id", 1L)
                                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().json(mapper.writeValueAsString(dtoList)));
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    @Test
    @DirtiesContext
    void getBookingCurrentUserPast() throws Exception {

        try {
            createBooking();

            List<BookingApproveDto> dtoList = bookingService
                    .getBookingByCurrentUser(1L, "PAST", 1, 20);

            mockMvc.perform(
                            MockMvcRequestBuilders.get("/bookings?state=PAST")
                                    .characterEncoding(StandardCharsets.UTF_8)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .header("X-Sharer-User-Id", 1L)
                                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().json(mapper.writeValueAsString(dtoList)));
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    @Test
    @DirtiesContext
    void getBookingCurrentUserRejected() throws Exception {

        try {
            createBooking();

            List<BookingApproveDto> dtoList = bookingService
                    .getBookingByCurrentUser(1L, "REJECTED", 1, 20);

            mockMvc.perform(
                            MockMvcRequestBuilders.get("/bookings?state=REJECTED")
                                    .characterEncoding(StandardCharsets.UTF_8)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .header("X-Sharer-User-Id", 1L)
                                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().json(mapper.writeValueAsString(dtoList)));
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    @Test
    @DirtiesContext
    void getBookingCurrentUserWaiting() throws Exception {

        try {
            createBooking();

            List<BookingApproveDto> dtoList = bookingService
                    .getBookingByCurrentUser(1L, "WAITING", 1, 20);

            mockMvc.perform(
                            MockMvcRequestBuilders.get("/bookings?state=WAITING")
                                    .characterEncoding(StandardCharsets.UTF_8)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .header("X-Sharer-User-Id", 1L)
                                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().json(mapper.writeValueAsString(dtoList)));
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    @Test
    @DirtiesContext
    void getBookingCurrentUserShouldReturnUnsupportedStatusExceptionException() throws Exception {
        try {
            createBooking();

            mockMvc.perform(
                            MockMvcRequestBuilders.get("/bookings?state=ERROR")
                                    .characterEncoding(StandardCharsets.UTF_8)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .header("X-Sharer-User-Id", 1L)
                                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isInternalServerError());
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    @Test
    @DirtiesContext
    void getBookingCurrentUserShouldReturnException() throws Exception {
        try {
            createBooking();

            List<BookingApproveDto> dtoList = bookingService
                    .getBookingByCurrentUser(1L, "ALL", 1, 20);

            mockMvc.perform(
                            MockMvcRequestBuilders.get("/bookings?state=ALL")
                                    .characterEncoding(StandardCharsets.UTF_8)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .header("X-Sharer-User-Id", 100L)
                                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    @Test
    @DirtiesContext
    void getBookingCurrentOwnerAll() throws Exception {

        try {
            createBooking();

            List<BookingApproveDto> dtoList = bookingService
                    .getBookingByCurrentOwner(1L, "ALL", 1, 20);

            mockMvc.perform(
                            MockMvcRequestBuilders.get("/bookings/owner?state=ALL")
                                    .characterEncoding(StandardCharsets.UTF_8)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .header("X-Sharer-User-Id", 1L)
                                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().json(mapper.writeValueAsString(dtoList)));
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }


    @Test
    @DirtiesContext
    void getBookingCurrentOwnerFuture() throws Exception {

        try {
            createBooking();
            List<BookingApproveDto> dtoList = bookingService
                    .getBookingByCurrentOwner(1L, "FUTURE", 1, 20);

            mockMvc.perform(
                            MockMvcRequestBuilders.get("/bookings/owner?state=FUTURE")
                                    .characterEncoding(StandardCharsets.UTF_8)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .header("X-Sharer-User-Id", 1L)
                                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().json(mapper.writeValueAsString(dtoList)));
        } catch (RuntimeException e) {
            e.printStackTrace();
        }

    }


    @Test
    @DirtiesContext
    void getBookingCurrentOwnerCurrent() throws Exception {

        try {
            createBooking();
            List<BookingApproveDto> dtoList = bookingService
                    .getBookingByCurrentOwner(1L, "CURRENT", 1, 20);

            mockMvc.perform(
                            MockMvcRequestBuilders.get("/bookings/owner?state=CURRENT")
                                    .characterEncoding(StandardCharsets.UTF_8)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .header("X-Sharer-User-Id", 1L)
                                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().json(mapper.writeValueAsString(dtoList)));
        } catch (RuntimeException e) {
            e.printStackTrace();
        }

    }


    @Test
    @DirtiesContext
    void getBookingCurrentOwnerPast() throws Exception {

        try {
            createBooking();
            List<BookingApproveDto> dtoList = bookingService
                    .getBookingByCurrentOwner(1L, "PAST", 1, 20);

            mockMvc.perform(
                            MockMvcRequestBuilders.get("/bookings/owner?state=PAST")
                                    .characterEncoding(StandardCharsets.UTF_8)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .header("X-Sharer-User-Id", 1L)
                                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().json(mapper.writeValueAsString(dtoList)));
        } catch (RuntimeException e) {
            e.printStackTrace();
        }

    }

    @Test
    @DirtiesContext
    void getBookingCurrentOwnerApproved() throws Exception {

        try {
            createBooking();

            List<BookingApproveDto> dtoList = bookingService
                    .getBookingByCurrentOwner(1L, "APPROVED", 1, 20);

            mockMvc.perform(
                            MockMvcRequestBuilders.get("/bookings/owner?state=APPROVED")
                                    .characterEncoding(StandardCharsets.UTF_8)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .header("X-Sharer-User-Id", 1L)
                                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().json(mapper.writeValueAsString(dtoList)));
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    @Test
    @DirtiesContext
    void getBookingCurrentOwnerApprovedShouldReturnException() throws Exception {

        try {
            createBooking();

            Booking booking1 = bookingRepository.findById(1L).get();
            booking1.setStatus(BookingStatus.REJECTED);
            bookingRepository.save(booking1);
            Booking booking2 = bookingRepository.findById(1L).get();
            booking2.setStatus(BookingStatus.REJECTED);
            bookingRepository.save(booking2);

            List<BookingApproveDto> dtoList = bookingService
                    .getBookingByCurrentOwner(1L, "APPROVED", 1, 20);

            mockMvc.perform(
                            MockMvcRequestBuilders.patch("/bookings/owner?approved=true")
                                    .characterEncoding(StandardCharsets.UTF_8)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .header("X-Sharer-User-Id", 1L)
                                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }


    @Test
    @DirtiesContext
    void getBookingCurrentOwnerShouldReturnUnsupportedStatusExceptionException()
            throws Exception {

        try {
            createBooking();

            mockMvc.perform(
                            MockMvcRequestBuilders
                                    .get("/bookings/owner?approved=true&state=ERROR")
                                    .characterEncoding(StandardCharsets.UTF_8)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .header("X-Sharer-User-Id", 1L)
                                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isInternalServerError());
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    private void createBooking() {
        try {
            userRepository.save(userMapper.toUser(userDto));
            userRepository.save(userMapper.toUser(userDto2));
            userRepository.save(userMapper.toUser(userDto3));

            itemService.create(1L, itemDto);
            itemService.create(2L, itemDto2);

            bookingDto.setItemId(1L);
            bookingDto2.setItemId(2L);


            LocalDateTime start = LocalDateTime.now().plusMinutes(1L);
            LocalDateTime end = LocalDateTime.now().plusMinutes(2L);

            bookingDto.setStart(start);
            bookingDto.setEnd(end);
            bookingDto.setStatus(BookingStatus.WAITING);

            bookingDto2.setStart(start.plusMinutes(1));
            bookingDto2.setEnd(end.plusMinutes(1));
            bookingDto2.setStatus(BookingStatus.WAITING);

            bookingService.create(2L, bookingDto);
            bookingService.create(1L, bookingDto2);
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }
}