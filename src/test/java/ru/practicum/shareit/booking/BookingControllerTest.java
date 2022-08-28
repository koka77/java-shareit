package ru.practicum.shareit.booking;

import org.hibernate.LazyInitializationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.practicum.shareit.AbstractControllerTest;
import ru.practicum.shareit.booking.dto.BookingApproveDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.requests.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@DirtiesContext
class BookingControllerTest extends AbstractControllerTest {
    @Autowired
    BookingService bookingService;

    @Autowired
    BookingMapper bookingMapper;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ItemRequestRepository requestRepository;

    @Autowired
    BookingController bookingController;



    @Autowired
    UserService userService;

    @Test
    @DirtiesContext
    void createShouldReturnItemNotAvailableException() throws Exception {

        userService.create(userDto);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/bookings")
                                .content(objectToJson(bookingDto))
                                .characterEncoding(StandardCharsets.UTF_8)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("X-Sharer-User-Id", 1L)
                                .accept(MediaType.APPLICATION_JSON));
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
                                .accept(MediaType.APPLICATION_JSON));
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
                                .accept(MediaType.APPLICATION_JSON));
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
    void approveStatusIsBad() throws Exception {

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
    }

    @Test
    @DirtiesContext
    @ExceptionHandler(LazyInitializationException.class)
    void getBookingById() throws Exception {
        createBooking();
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/bookings/1")
                                .characterEncoding(StandardCharsets.UTF_8)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("X-Sharer-User-Id", 1L)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DirtiesContext
    void getBookingByIdShouldReturnUserHasNotPermissionException() throws Exception {
        createBooking();
        BookingApproveDto dto = bookingService.getBookingById(1L, 1L);
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


        mockMvc.perform(
                        MockMvcRequestBuilders.get("/bookings?state=ALL")
                                .characterEncoding(StandardCharsets.UTF_8)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("X-Sharer-User-Id", 1L)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }


    @Test
    @DirtiesContext
    void getBookingCurrentUserFuture() throws Exception {
        createBooking();

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/bookings?state=FUTURE")
                                .characterEncoding(StandardCharsets.UTF_8)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("X-Sharer-User-Id", 1L)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DirtiesContext
    void getBookingCurrentUserCurrent() throws Exception {
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
    }

    @Test
    @DirtiesContext
    void getBookingCurrentUserPast() throws Exception {
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
    }

    @Test
    @DirtiesContext
    void getBookingCurrentUserRejected() throws Exception {
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
    }

    @Test
    @DirtiesContext
    void getBookingCurrentUserWaiting() throws Exception {
        createBooking();

         mockMvc.perform(
                        MockMvcRequestBuilders.get("/bookings?state=WAITING")
                                .characterEncoding(StandardCharsets.UTF_8)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("X-Sharer-User-Id", 1L)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
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
                .getBookingByCurrentUser(1L, "ALL", 1, 20);

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

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/bookings/owner?state=ALL")
                                .characterEncoding(StandardCharsets.UTF_8)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("X-Sharer-User-Id", 1L)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }


    @Test
    @DirtiesContext
    void getBookingCurrentOwnerFuture() throws Exception {
        createBooking();

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/bookings/owner?state=FUTURE")
                                .characterEncoding(StandardCharsets.UTF_8)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("X-Sharer-User-Id", 1L)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }


    @Test
    @DirtiesContext
    void getBookingCurrentOwnerCurrent() throws Exception {
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
    }


    @Test
    @DirtiesContext
    void getBookingCurrentOwnerPast() throws Exception {
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
    }

    @Test
    @DirtiesContext
    void getBookingCurrentOwnerApproved() throws Exception {
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
    }

    @Test
    @DirtiesContext
    void getBookingCurrentOwnerApprovedShouldReturnException() throws Exception {
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
    }


    @Test
    @DirtiesContext
    void getBookingCurrentOwnerShouldReturnUnsupportedStatusExceptionException()
            throws Exception {
        createBooking();

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get("/bookings/owner?approved=true&state=ERROR")
                                .characterEncoding(StandardCharsets.UTF_8)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("X-Sharer-User-Id", 1L)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @DirtiesContext
    private void createBooking() {

        LocalDateTime start = LocalDateTime.now().plusMinutes(1L);
        LocalDateTime end = LocalDateTime.now().plusMinutes(2L);

        userRepository.deleteAll();
        itemRepository.deleteAll();
        bookingRepository.deleteAll();

        User user1 = new User();
        user1.setName("1");
        user1.setEmail("1@1.ru");

        User user2 = new User();
        user2.setName("2");
        user2.setEmail("2@2.ru");

        User user3 = new User();
        user3.setName("3");
        user3.setEmail("3@3.ru");

        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);

        ItemRequest request1 = new ItemRequest();
        request1.setCreated(start);
        request1.setRequestor(user1);
        request1.setDescription("descReq1");

        ItemRequest request2 = new ItemRequest();
        request2.setCreated(start);
        request2.setRequestor(user2);
        request2.setDescription("descReq2");

        requestRepository.save(request1);
        requestRepository.save(request2);

        Item item1 = new Item();
        item1.setOwner(user1);
        item1.setName("Item1");
        item1.setDescription("descr1");
        item1.setAvailable(true);
        item1.setRequest(request1);

        Item item2 = new Item();
        item2.setOwner(user2);
        item2.setName("Item2");
        item2.setDescription("descr2");
        item2.setAvailable(true);
        item2.setRequest(request2);

        itemRepository.save(item1);
        itemRepository.save(item2);


        Booking booking1 = new Booking();
        booking1.setBooker(user1);
        booking1.setItem(item1);
        booking1.setStart(start);
        booking1.setEnd(end);
        booking1.setStatus(BookingStatus.WAITING);


        Booking booking2 = new Booking();
        booking2.setBooker(user2);
        booking2.setItem(item2);
        booking2.setStart(start);
        booking2.setEnd(end);
        booking2.setStatus(BookingStatus.WAITING);

        bookingRepository.save(booking1);
        bookingRepository.save(booking2);

    }
}