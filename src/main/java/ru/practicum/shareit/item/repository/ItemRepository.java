package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ItemRepository {

    Optional<Item> create(Item item);

    void deleteById(Long id);

    Item update(Item item);

    Collection<Item> findAll();

    Optional<Item> findById(Long id);

    List<Item> search(Long ownerId, String text);
}
