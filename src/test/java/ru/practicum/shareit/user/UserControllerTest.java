package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.practicum.shareit.AbstractControllerTest;
import ru.practicum.shareit.user.dto.UserDto;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DirtiesContext
class UserControllerTest extends AbstractControllerTest {
    @Autowired
    UserController userController;


    @Test
    void shouldCreateUserCorrectly() throws Exception {

        userDto.setName("2update");
        userDto.setEmail("2update@user.com");
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/users")
                                .content(objectToJson(userDto))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content()
                        .json("{\"id\":4,\"name\":\"2update\",\"email\":\"2update@user.com\"}"));
    }

    @Test
    void shouldUpdateUserCorrectly() throws Exception {

        userController.create(userDto);
        UserDto updateUser = UserDto.builder().name("update").email("update@user.com").build();
        mockMvc.perform(
                        MockMvcRequestBuilders.patch("/users/{userId}", 1l)
                                .content(objectToJson(updateUser))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content()
                        .json("{\"id\":1,\"name\":\"update\",\"email\":\"update@user.com\"}"));
    }

    @Test
    void shouldReturnAllUsersCorrectly() throws Exception {
        
        userController.create(userDto);
        userDto.setEmail("all@all.la");
        userController.create(userDto);
        mockMvc.perform(MockMvcRequestBuilders.get("/users"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content()
                        .json("[{\"id\":1,\"name\":\"update\",\"email\":\"update@user.com\"}," +
                                "{\"id\":2,\"name\":\"1update\",\"email\":\"1update@user.com\"}," +
                                "{\"id\":3,\"name\":\"1update\",\"email\":\"all@all.la\"}]"));
    }

    @Test
    void shouldFindByIdCorrectly() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/users/{userId}", 1l))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content()
                        .json("{\"id\":1,\"name\":\"update\",\"email\":\"update@user.com\"}"));
    }

    @Test
    void shouldDeleteUserCorrectly() throws Exception {

        mockMvc.perform(
                        MockMvcRequestBuilders.delete("/users/{userId}", 1l)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }
}