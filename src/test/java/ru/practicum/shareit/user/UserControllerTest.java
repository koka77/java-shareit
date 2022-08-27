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
                        .json("{\"id\":2,\"name\":\"2update\",\"email\":\"2update@user.com\"}"));
    }

    @Test
    void shouldUpdateUserCorrectly() throws Exception {

        userController.create(userDto);
        UserDto updateUser = UserDto.builder().name("3update").email("3update@user.com").build();
        mockMvc.perform(
                        MockMvcRequestBuilders.patch("/users/{userId}", 1l)
                                .content(objectToJson(updateUser))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content()
                        .json("{\"id\":1,\"name\":\"3update\",\"email\":\"3update@user.com\"}"));
    }

    @Test
    void shouldReturnAllUsersCorrectly() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/users"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content()
                        .json("[{\"id\":1,\"name\":\"3update\",\"email\":\"3update@user.com\"}]"));
    }

    @Test
    void shouldFindByIdCorrectly() throws Exception {

        userController.create(userDto3);
        UserDto updateUser = UserDto.builder().name("3update").email("3update@user.com").build();
        mockMvc.perform(
                        MockMvcRequestBuilders.patch("/users/{userId}", 1l)
                                .content(objectToJson(updateUser))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content()
                        .json("{\"id\":1,\"name\":\"3update\",\"email\":\"3update@user.com\"}"));
    }

}