package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.AbstractControllerTest;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DirtiesContext
@Transactional
class ItemControllerTest extends AbstractControllerTest {

    @Autowired
    protected UserService userService;


    @Autowired
    ItemController itemController;

    @Test
    @DirtiesContext
    void shouldCreateItemCorrectly() throws Exception {
        prepair();

        ItemRequest request = itemRequestRepository.findById(1L).get();
        mockMvc.perform(MockMvcRequestBuilders.post("/items").content(objectToJson(itemDto))
                .contentType(MediaType.APPLICATION_JSON).header("X-Sharer-User-Id", 1L)).andExpect(status().isOk())
                .andDo(print()).andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content()
                        .json("{\"id\":3,\"name\":\"Дрель\",\"description\":\"Простая дрель\",\"available\":true,\"requestId\":1,\"request\":" + mapper
                                .writeValueAsString(request) + ",\"nextBooking\":null,\"lastBooking\":null,\"comments\":null}"));
    }

    @Test
    @DirtiesContext
    void shouldCreateItemCorrectlyWithRequestId() throws Exception {
        prepair();
        itemDto.setRequestId(1L);
        mockMvc.perform(MockMvcRequestBuilders.post("/items")
                .content(objectToJson(itemDto)).contentType(MediaType.APPLICATION_JSON)
                .header("X-Sharer-User-Id", 1L)).andExpect(status().isOk())
                .andDo(print()).andExpect(content()
                .contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

    }


    @Test
    @DirtiesContext
    void shouldUpdateItemCorrectly() throws Exception {

        prepair();
        ItemDto updateItem = ItemDto.builder().name("Аккумуляторная дрель").build();

        ItemDto expected = itemService.findById(1L, 1L);
        expected.setName("Аккумуляторная дрель");
        expected.setComments(null);
        mockMvc.perform(MockMvcRequestBuilders.patch("/items/{itemId}", 1L).content(objectToJson(updateItem))
                .contentType(MediaType.APPLICATION_JSON).header("X-Sharer-User-Id", 1L)).andExpect(status().isOk())
                .andDo(print()).andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(mapper.writeValueAsString(expected)));
    }

    @Test
    @DirtiesContext
    void shouldReturnAllItemCorrectly() throws Exception {

        prepair();

        Collection<ItemDto> all = itemService.findAll(1L);
        mockMvc.perform(MockMvcRequestBuilders.get("/items").header("X-Sharer-User-Id", 1L)).andExpect(status().isOk())
                .andDo(print()).andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(mapper.writeValueAsString(all)));
    }

    @Test
    @DirtiesContext
    void shouldReturnItemById() throws Exception {
        prepair();
        ItemDto dto = itemService.findById(1L, 1L);
        mockMvc.perform(MockMvcRequestBuilders.get("/items/{itemId}", 1L).param("ownerId", "1")
                .header("X-Sharer-User-Id", 1L)).andExpect(status().isOk()).andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(mapper.writeValueAsString(dto)));
    }

    @Test
    @DirtiesContext
    void shouldSearchItem() throws Exception {

        prepair();
        Collection<ItemDto> items = itemService.search("дРелЬ");
        mockMvc.perform(MockMvcRequestBuilders.get("/items/search", 1L).param("text", "дРелЬ")
                .header("X-Sharer-User-Id", 1L)).andExpect(status().isOk()).andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(mapper.writeValueAsString(items)));
    }

    @Test
    @DirtiesContext
    void searchItemShouldReturnEmptyList() throws Exception {

        prepair();
        mockMvc.perform(MockMvcRequestBuilders.get("/items/search", 1L)
                .param("text", "")
                .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk()).andDo(print())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(mapper.writeValueAsString(new ArrayList<>())));
    }


    @Test
    @DirtiesContext
    void shouldCreateComment() throws Exception {
        prepair();

        CommentDto dto = new CommentDto();
        LocalDateTime created = LocalDateTime.now();
        dto.setCreated(created);
        dto.setItem(itemMapper.toItem(itemDto));
        dto.setAuthorName("lalala");
        dto.setText("bbbbbb");
        dto.getItem().setId(1L);


        mockMvc.perform(MockMvcRequestBuilders.post("/items/2/comment").contentType(MediaType.APPLICATION_JSON)
                .header("X-Sharer-User-Id", 1L).content(objectToJson(dto)))

                .andExpect(status().isOk()).andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @DirtiesContext
    private void prepair() {
        userDto = userService.create(userDto);
        userDto3 = userService.create(userDto3);
        User user = userMapper.toUser(userDto);
        ItemRequest request = new ItemRequest();

        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = LocalDateTime.now().plusMinutes(1);
        request.setRequestor(user);
        request.setDescription("test");
        request.setCreated(start);
        itemRequestRepository.save(request);
        itemService.create(1L, itemDto);


        Item item = itemMapper.toItem(itemDto);
        item.setOwner(user);
        item.getOwner().setId(1L);
        item = itemRepository.save(item);

        Booking booking = new Booking();
        booking.setStatus(BookingStatus.WAITING);
        booking.setItem(item);
        booking.setBooker(userMapper.toUser(userDto));
        booking.setStart(start);
        booking.setEnd(end);
        booking.getBooker().setId(1L);
        bookingRepository.save(booking);


//        return itemService.createComment(userDto.getId(), item.getId(), commentDto);
    }
}