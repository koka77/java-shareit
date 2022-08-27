package ru.practicum.shareit.requests;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.AbstractControllerTest;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.exception.RequestNotFoundException;
import ru.practicum.shareit.requests.service.ItemRequestServiceImpl;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.service.UserService;

import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DirtiesContext
@SpringBootTest
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemRequestControllerTest extends AbstractControllerTest {
    @Autowired
    private final ItemRequestServiceImpl itemRequestService;

    @Autowired
    private final UserService userService;

    @BeforeEach
    private void init() {
        userService.create(userDto);
    }


    @Test
    void create() {
    }

    @Test
    void getByRequestor() {
    }

    @Test
    void getAllShouldReturnUserNotFoundException() {
        assertThrows(UserNotFoundException.class, () -> itemRequestService.getAll( 100L, 1, 1));
    }

    @Test
    void getAllShouldReturnNothing() {
        userDto.setEmail("222@321.ru");
        userService.create(userDto);
        assertEquals(itemRequestService.getAll( 8L, 1, 1), new ArrayList<>());
    }

    @Test
    void getById() {
        Long userId = 5l;
        userDto.setEmail("123@321.ru");
        userService.create(userDto);
        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setItems(new ArrayList<>());
                itemRequestDto.setDescription("1");
        itemRequestService.create(5l, itemRequestDto);
        ItemRequestDto receivedRequestDto = itemRequestService.findByIdDto(1l , userId);
        assertThat(receivedRequestDto.getId(), equalTo(1l));
        assertThat(receivedRequestDto.getDescription(), equalTo(itemRequestDto.getDescription()));
    }
    @Test
    void getByIdShouldReturnUserNotFoundException() {
        assertThrows(UserNotFoundException.class, () -> itemRequestService.findByIdDto(1L, 100L));
    }
}