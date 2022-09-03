package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.AbstractControllerTest;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.exceptions.ItemNotFoundException;
import ru.practicum.shareit.item.exceptions.UserNotBookerException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import static org.junit.jupiter.api.Assertions.assertThrows;

@DirtiesContext
public class ItemServiceTest extends AbstractControllerTest {

    @Autowired
    protected UserService userService;

    @Test
    void sholdThrowsItemNotFoundException() {
        assertThrows(ItemNotFoundException.class, () -> itemService.findById(100L, 1L));

    }

    @Test
    void shouldThrowsUserNotBookerException() {
        UserDto userDto = userService.create(userDto2);
        itemDto.setRequestId(null);
        itemDto.setRequest(null);
        itemService.create(userDto.getId(), itemDto);
        assertThrows(UserNotBookerException.class, () -> itemService
                .createComment(userDto.getId(), 1L, new CommentDto()));

    }
}
