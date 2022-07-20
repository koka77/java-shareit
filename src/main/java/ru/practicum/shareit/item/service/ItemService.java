package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Collection;
import java.util.Optional;

public interface ItemService {

    Optional<ItemDto> create(long userId, ItemDto itemDto);

    void deleteById(Long id);

    Optional<ItemDto> updateById(Long userId, Long id, ItemDto itemDto);

    Collection<ItemDto> findAll(Long ownerId);

    Optional<ItemDto> findById(Long id);

    Collection<ItemDto> searchItem(Long ownerId, String text);
}
