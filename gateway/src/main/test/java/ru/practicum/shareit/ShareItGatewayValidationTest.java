package ru.practicum.shareit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.bind.MethodArgumentNotValidException;
import ru.practicum.shareit.booking.BookingClient;
import ru.practicum.shareit.booking.BookingController;
import ru.practicum.shareit.item.ItemClient;
import ru.practicum.shareit.item.ItemController;
import ru.practicum.shareit.request.RequestClient;
import ru.practicum.shareit.request.RequestController;
import ru.practicum.shareit.user.UserClient;
import ru.practicum.shareit.user.UserController;

import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {
        UserController.class,
        RequestController.class,
        ItemController.class,
        BookingController.class
})
class ShareItGatewayValidationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    UserClient userClient;

    @MockBean
    RequestClient requestClient;

    @MockBean
    ItemClient itemClient;

    @MockBean
    BookingClient bookingClient;


    @Test
    void usersShouldReturnBadRequestAndThrowsMethodArgumentNotValidException() throws Exception {
        mockMvc.perform(post("/users")
                        .content("{\n" +
                                "    \"name\": \"user\"\n" +
                                "}")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException()
                        instanceof MethodArgumentNotValidException));
    }

    @Test
    void requestShouldReturnBadRequestAndThrowsMethodArgumentNotValidException() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/requests")
                                .content("{\n" +
                                        "    \"description\": null\n" +
                                        "}")
                                .characterEncoding(StandardCharsets.UTF_8)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("X-Sharer-User-Id", 1L)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException()
                        instanceof MethodArgumentNotValidException));
    }

    @Test
    void itemsShouldReturnBadRequestAndThrowsMethodArgumentNotValidException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/items").content("{\n" +
                                "    \"name\": \"Отвертка\",\n" +
                                "    \"description\": \"Аккумуляторная отвертка\"\n" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON).header("X-Sharer-User-Id",
                                1L))
                .andExpect(status().isBadRequest())
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException()
                        instanceof MethodArgumentNotValidException));
    }

    @Test
    void bookingsShouldReturnBadRequestAndThrowsMethodArgumentNotValidException() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/bookings")
                                .content("{\n" +
                                        "    \"itemId\": 1,\n" +
                                        "    \"start\": \"" + "2021-01-01T10:10:10"
                                        + "\",\n" +
                                        "    \"end\": \"2023-01-01T10:10:10\"\n" +
                                        "}")
                                .characterEncoding(StandardCharsets.UTF_8)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("X-Sharer-User-Id", 1L)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()).andExpect(result -> Assertions
                        .assertTrue(result.getResolvedException()
                                instanceof MethodArgumentNotValidException));
    }
}