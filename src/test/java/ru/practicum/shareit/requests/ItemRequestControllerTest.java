package ru.practicum.shareit.requests;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.practicum.shareit.AbstractControllerTest;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.requests.service.ItemRequestServiceImpl;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.service.UserService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DirtiesContext
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemRequestControllerTest extends AbstractControllerTest {

    @Autowired
    private final ItemRequestServiceImpl itemRequestService;

    @Autowired
    private final UserService userService;


    @Test
    @DirtiesContext
    void create() throws Exception {

        prepairRequest();
        LocalDateTime start = LocalDateTime.now().plusMinutes(1L);
        ItemRequest request = new ItemRequest();
        request.setRequestor(userMapper.toUser(userDto));
        request.setDescription("one");
        request.getRequestor().setId(1L);
        request.setCreated(start);
        ItemRequestDto dto = itemRequestMapper.toItemRequestDto(request);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/requests")
                        .content(objectToJson(dto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DirtiesContext
    void getByRequestor() throws Exception {
        prepairRequest();
        LocalDateTime start = LocalDateTime.now().plusMinutes(1L);
        ItemRequest request = new ItemRequest();
        request.setRequestor(userMapper.toUser(userDto));
        request.setDescription("one");
        request.getRequestor().setId(1L);
        request.setCreated(start);

        itemRequestRepository.save(request);

        ItemRequestDto expected = itemRequestService.findByIdDto(1L, 1L);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/requests")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(List.of(expected))));
    }


    @Test
    @DirtiesContext
    void getAllShouldReturnUserNotFoundException() throws Exception {
        assertThrows(UserNotFoundException.class, () -> itemRequestService.getAll(100L, 1, 1));
    }

    @Test
    @DirtiesContext
    void getAllShouldReturnItemListCorrectly() throws Exception {
        prepairRequest();

        List<ItemRequestDto> expected = itemRequestService.getAll(1L, 1, 20);

        LocalDateTime start = LocalDateTime.now().plusMinutes(1L);
        ItemRequest request = new ItemRequest();
        request.setRequestor(userMapper.toUser(userDto));
        request.setDescription("one");
        request.getRequestor().setId(1L);
        request.setCreated(start);
        mockMvc.perform(
                MockMvcRequestBuilders.get("/requests/all")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(expected)));
        ;
    }

    @Test
    @DirtiesContext
    void getAllShouldReturnNothing() throws Exception {
        prepairRequest();
        LocalDateTime start = LocalDateTime.now().plusMinutes(1L);
        ItemRequest request = new ItemRequest();
        request.setRequestor(userMapper.toUser(userDto));
        request.setDescription("one");
        request.getRequestor().setId(1L);
        request.setCreated(start);
        itemRequestRepository.save(request);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/requests")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 2L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(new ArrayList<>())));
    }


    @Test
    @DirtiesContext
    void getAllShouldCallitemRequestDtoSetItems() throws Exception {
        prepairRequest();
        LocalDateTime start = LocalDateTime.now().plusMinutes(1L);
        ItemRequest request = new ItemRequest();
        request.setRequestor(userMapper.toUser(userDto));
        request.setDescription("one");
        request.getRequestor().setId(1L);
        request.setCreated(start);
        itemRequestRepository.save(request);

        List<ItemRequestDto> expected = itemRequestService.getAll(2L, 1, 10);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/requests/all")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 2L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(expected)));
    }

    @Test
    @DirtiesContext
    void getById() throws Exception {
        prepairRequest();

        LocalDateTime start = LocalDateTime.now().plusMinutes(1L);
        ItemRequest request = new ItemRequest();
        request.setRequestor(userMapper.toUser(userDto));
        request.setDescription("one");
        request.getRequestor().setId(1L);
        request.setCreated(start);
        itemRequestMapper.toItemRequestDto(itemRequestRepository.save(request));

        ItemRequestDto expected = itemRequestService.findByIdDto(1L, 1L);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/requests/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(expected)));
    }

    @Test
    @DirtiesContext
    void getByIdShouldReturnUserNotFoundException() {
        assertThrows(UserNotFoundException.class, () -> itemRequestService.findByIdDto(1L, 100L));
    }

    private void prepairRequest() {

        userDto = userService.create(userDto);
        userDto2 = userService.create(userDto2);
        userDto3 = userService.create(userDto3);


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
    }
}