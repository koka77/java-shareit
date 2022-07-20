package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.practicum.shareit.AbstractControllerTest;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.UserController;
import ru.practicum.shareit.user.service.UserService;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DirtiesContext
class ItemControllerTest extends AbstractControllerTest {

    @Autowired
    ItemController itemController;

    @BeforeAll
    private static void init(@Autowired UserService userService) {
        userService.create(userDto);
    }

    @Test
    void shouldCreateItemCorrectly() throws Exception {



        mockMvc.perform(
                        MockMvcRequestBuilders.post("/items")
                                .content(objectToJson(itemDto))
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("X-Sharer-User-Id", 1L)
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content()
                        .json("{\"id\":2,\"name\":\"Дрель\",\"description\":\"Простая дрель\",\"available\":true,\"owner\":{\"id\":1,\"name\":\"1update\",\"email\":\"1update@user.com\"},\"request\":null}"));
    }


    @Test
    void shouldUpdateItemCorrectly() throws Exception {


        itemController.create(1l, itemDto);
        ItemDto updateItem = ItemDto.builder().name("Аккумуляторная дрель").build();
        mockMvc.perform(
                        MockMvcRequestBuilders.patch("/items/{itemId}", 1l)
                                .content(objectToJson(updateItem))
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("X-Sharer-User-Id", 1L)
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content()
                        .json("{\"id\":1,\"name\":\"Аккумуляторная дрель\",\"description\":\"Простая дрель\",\"available\":true,\"owner\":{\"id\":1,\"name\":\"1update\",\"email\":\"1update@user.com\"},\"request\":null}"));
    }

    @Test
    void shouldFindAllItemCorrectly() {
    }

    @Test
    void houldFindItemById() {
    }

    @Test
    void houldSearchItem() {
    }


    @Test
    void shouldDeleteItemByIdCorrectly() {
    }
}