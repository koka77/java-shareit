package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.List;

public interface ItemService {

    ItemDto create(long userId, ItemDto itemDto);

    void deleteById(Long id);

    ItemDto updateById(Long userId, Long id, ItemDto itemDto);

    Collection<ItemDto> findAll(Long ownerId);

    ItemDto findById(Long id, Long ownerId);

    Collection<ItemDto> search(String text);

    CommentDto createComment(long userId, long itemId, CommentDto commentDto);

    List<Item> findByRequestor(User requestor);
}
