package ru.practicum.shareit.mappers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.AbstractControllerTest;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ItemMapperTest extends AbstractControllerTest {

    @Autowired
    ItemMapper itemMapper;

    @Test
    @DirtiesContext
    void shouldUpdateItemFromDtoCorrectly() {
        Item item = new Item();
        itemMapper.updateItemFromDto(itemDto, item);
        assertEquals(item.getName(), itemDto.getName());
    }

    @Test
    @DirtiesContext
    void shouldReturnNull() {
        assertNull(itemMapper.toItemDto(null));
        assertNull(itemMapper.toItem(null));
        assertNull(itemMapper.toDtoList(null));
    }
}
