package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.Collection;
import java.util.Optional;

public interface ItemService {

    Optional<ItemDto> create(long userId, ItemDto itemDto);

    void deleteById(Long id);

    Optional<ItemDto> updateById(Long id, ItemDto itemDto);

    Collection<ItemDto> findAll();

    Optional<ItemDto> findById(Long id);
}
