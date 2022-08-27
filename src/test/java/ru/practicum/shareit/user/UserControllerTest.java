package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.practicum.shareit.AbstractControllerTest;
import ru.practicum.shareit.user.dto.UserDto;

import javax.transaction.Transactional;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DirtiesContext
@Transactional
class UserControllerTest extends AbstractControllerTest {
    @Autowired
    UserController userController;

    @Test
    @DirtiesContext
    void shouldDeleteUserCorrectly() throws Exception {
        userController.create(userDto);
        mockMvc.perform(
                        MockMvcRequestBuilders.delete("/users/1"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(status().isOk());
    }


    @Test
    @DirtiesContext
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
                        .json("{\"id\":1,\"name\":\"2update\",\"email\":\"2update@user.com\"}"));
    }

    @Test
    @DirtiesContext
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
    @DirtiesContext
    void shouldReturnAllUsersCorrectly() throws Exception {
        userController.create(userDto);
        mockMvc.perform(MockMvcRequestBuilders.get("/users"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content()
                        .json("[{\"id\":1,\"name\":\"1update\",\"email\":\"1update@user.com\"}]"));
    }

    @Test
    @DirtiesContext
    void shouldFindByIdCorrectly() throws Exception {

        userController.create(userDto3);
        UserDto updateUser = UserDto.builder().name("3update").email("3update@user.com").build();
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/users/{userId}", 1l)
                                .content(objectToJson(updateUser))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content()
                        .json("{\"id\":1,\"name\":\"4update\",\"email\":\"4update@user.com\"}"));
    }

}