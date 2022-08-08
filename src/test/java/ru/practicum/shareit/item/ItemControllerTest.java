package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.practicum.shareit.AbstractControllerTest;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.service.UserService;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DirtiesContext
class ItemControllerTest extends AbstractControllerTest {

    private static UserService userService;

    @Autowired
    ItemController itemController;

  /*  @BeforeAll
    private static void init(@Autowired UserService userService) {

        ItemControllerTest.userService = userService;
        if (userService.findAll().isEmpty()) {
            userDto.setEmail("update@user.com");
            userDto.setName("update");
            userService.create(userDto);
        }
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
                        .json("{\"id\":6,\"name\":\" for test2\",\"description\":\"Простая дрель\"," +
                                "\"available\":true,\"owner\":{\"id\":1,\"name\":\"update\"," +
                                "\"email\":\"update@user.com\"},\"request\":null}"));
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
                        .json("{\"id\":1,\"name\":\"Аккумуляторная дрель\",\"description\":\"Простая дрель\",\"available\":true,\"owner\":{\"id\":1,\"name\":\"update\",\"email\":\"update@user.com\"},\"request\":null}"));
    }

    @Test
    void shouldReturnAllItemCorrectly() throws Exception {

        itemController.create(1l, itemDto);
        itemDto.setName("for test1");
        itemController.create(1l, itemDto);
        itemDto.setName(" for test2");
        itemController.create(1l, itemDto);
        mockMvc.perform(MockMvcRequestBuilders.get("/items")
                        .header("X-Sharer-User-Id", 1L)
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content()
                        .json("[{\"id\":3,\"name\":\"Дрель\",\"description\":\"Простая дрель\"," +
                                "\"available\":true,\"owner\":{\"id\":1,\"name\":\"update\"," +
                                "\"email\":\"update@user.com\"},\"request\":null},{\"id\":2,\"name\":\"Дрель\"," +
                                "\"description\":\"Простая дрель\",\"available\":true,\"owner\":{\"id\":1," +
                                "\"name\":\"update\",\"email\":\"update@user.com\"},\"request\":null},{\"id\":1," +
                                "\"name\":\"Аккумуляторная дрель\",\"description\":\"Простая дрель\"," +
                                "\"available\":true,\"owner\":{\"id\":1,\"name\":\"update\"," +
                                "\"email\":\"update@user.com\"},\"request\":null},{\"id\":4,\"name\":\"for test1\"," +
                                "\"description\":\"Простая дрель\",\"available\":true,\"owner\":{\"id\":1," +
                                "\"name\":\"update\",\"email\":\"update@user.com\"},\"request\":null},{\"id\":5," +
                                "\"name\":\" for test2\",\"description\":\"Простая дрель\",\"available\":true," +
                                "\"owner\":{\"id\":1,\"name\":\"update\",\"email\":\"update@user.com\"}," +
                                "\"request\":null}]"));
    }

    @Test
    void shouldReturnItemById() throws Exception {

        itemController.create(1l, itemDto);
        mockMvc.perform(MockMvcRequestBuilders.get("/items/{itemId}", 1l))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content()
                        .json("{\"id\":1,\"name\":\"Дрель\",\"description\":\"Простая дрель\"," +
                                "\"available\":true,\"owner\":{\"id\":1,\"name\":\"update\"," +
                                "\"email\":\"update@user.com\"},\"request\":null}"));
    }

    @Test
    void shouldSearchItem() throws Exception {

        itemController.create(1l, itemDto);
        mockMvc.perform(MockMvcRequestBuilders.get("/items/search", 1l)
                        .param("text", "дРелЬ")
                        .header("X-Sharer-User-Id", 1L)
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content()
                        .json("[{\"id\":3,\"name\":\"Дрель\",\"description\":\"Простая дрель\"," +
                                "\"available\":true,\"owner\":{\"id\":1,\"name\":\"update\"," +
                                "\"email\":\"update@user.com\"},\"request\":null},{\"id\":7,\"name\":\" for test2\"," +
                                "\"description\":\"Простая дрель\",\"available\":true,\"owner\":{\"id\":1," +
                                "\"name\":\"update\",\"email\":\"update@user.com\"},\"request\":null}," +
                                "{\"id\":2,\"name\":\"Дрель\",\"description\":\"Простая дрель\"," +
                                "\"available\":true,\"owner\":{\"id\":1,\"name\":\"update\"," +
                                "\"email\":\"update@user.com\"},\"request\":null}," +
                                "{\"id\":6,\"name\":\" for test2\",\"description\":\"Простая дрель\"," +
                                "\"available\":true,\"owner\":{\"id\":1,\"name\":\"update\"," +
                                "\"email\":\"update@user.com\"},\"request\":null},{\"id\":1," +
                                "\"name\":\"Аккумуляторная дрель\",\"description\":\"Простая дрель\"," +
                                "\"available\":true,\"owner\":{\"id\":1,\"name\":\"update\"," +
                                "\"email\":\"update@user.com\"},\"request\":null},{\"id\":4,\"name\":\"for test1\"," +
                                "\"description\":\"Простая дрель\",\"available\":true,\"owner\":{\"id\":1," +
                                "\"name\":\"update\",\"email\":\"update@user.com\"},\"request\":null},{\"id\":5," +
                                "\"name\":\" for test2\",\"description\":\"Простая дрель\",\"available\":true," +
                                "\"owner\":{\"id\":1,\"name\":\"update\",\"email\":\"update@user.com\"}," +
                                "\"request\":null}]"));
    }


    @Test
    void shouldDeleteItemByIdCorrectly() throws Exception {

        mockMvc.perform(
                        MockMvcRequestBuilders.delete("/items/{itemId}", 1l)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }*/
}