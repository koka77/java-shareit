package ru.practicum.shareit.item.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;

import java.util.Collection;
import java.util.Optional;

@Service
public class ItemServiceImpl implements ItemService {

    private final ItemMapper itemMapper;
    private final ItemRepository itemRepository;

    @Autowired
    public ItemServiceImpl(ItemMapper itemMapper, ItemRepository itemRepository) {
        this.itemMapper = itemMapper;
        this.itemRepository = itemRepository;
    }

    @Override
    public Optional<ItemDto> create(ItemDto itemDto) {
        Item item = itemRepository.create(itemMapper.toItem(itemDto)).get();
        return Optional.of(itemMapper.toItemDto(item));
    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public Optional<ItemDto> updateById(Long id, ItemDto itemDto) {
        return Optional.empty();
    }

    @Override
    public Collection<ItemDto> findAll() {
        return null;
    }

    @Override
    public Optional<ItemDto> findById(Long id) {
        return Optional.empty();
    }
}
