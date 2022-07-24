package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Collection;

public interface ItemService {

    ItemDto create(long userId, ItemDto itemDto);

    void deleteById(Long id);

    ItemDto updateById(Long userId, Long id, ItemDto itemDto);

    Collection<ItemDto> findAll(Long ownerId);

    ItemDto findById(Long id);

    Collection<ItemDto> searchItem(Long ownerId, String text);
}
