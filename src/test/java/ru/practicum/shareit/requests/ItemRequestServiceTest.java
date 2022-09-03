package ru.practicum.shareit.requests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.AbstractControllerTest;
import ru.practicum.shareit.requests.exception.RequestNotFoundException;
import ru.practicum.shareit.requests.service.ItemRequestServiceImpl;
import ru.practicum.shareit.user.service.UserService;

import static org.junit.jupiter.api.Assertions.assertThrows;

@DirtiesContext
public class ItemRequestServiceTest extends AbstractControllerTest {
    @Autowired
    private ItemRequestServiceImpl itemRequestService;

    @Autowired
    protected UserService userService;


    @Test
    @DirtiesContext
    void shouldThrowsRequestNotFoundException() {

        userService.create(userDto);

        assertThrows(RequestNotFoundException.class, () -> itemRequestService
                .findByIdDto(100L, 1L));
    }
}
