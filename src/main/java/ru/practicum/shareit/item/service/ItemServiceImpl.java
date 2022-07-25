package ru.practicum.shareit.item.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {

    private final ItemMapper itemMapper;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Autowired
    public ItemServiceImpl(ItemMapper itemMapper, ItemRepository itemRepository, UserRepository userRepository) {
        this.itemMapper = itemMapper;
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ItemDto create(long userId, ItemDto itemDto) {
        try {

            itemDto.setOwner(userRepository.findById(userId).orElseThrow());
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return itemMapper.toItemDto(itemRepository.create(itemMapper.toItem(itemDto)));
    }

    @Override
    public void deleteById(Long id) {
        itemRepository.deleteById(id);
    }

    @Override
    public ItemDto updateById(Long userId, Long id, ItemDto itemDto) {

        Item result = itemRepository.findById(id).orElseThrow();
        if (!userId.equals(result.getOwner().getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        itemMapper.updateItemFromDto(itemDto, result);
        itemRepository.update(result);
        return itemMapper.toItemDto(result);
    }

    @Override
    public Collection<ItemDto> findAll(Long ownerId) {
        return itemRepository.findAll().stream().filter(item -> item.getOwner().getId().equals(ownerId))
                .map(itemMapper::toItemDto).collect(Collectors.toSet());
    }

    @Override
    public ItemDto findById(Long id) {
        ItemDto dto = itemMapper.toItemDto(itemRepository.findById(id).get());
        return dto;
    }

    @Override
    public Collection<ItemDto> searchItemByNameAndDescription(Long ownerId, String text) {
        if (text == null || text.isEmpty()) {
            return new ArrayList<>();
        }
        return itemRepository.searchItemByNameAndDescription(ownerId, text).stream().map(itemMapper::toItemDto).collect(Collectors.toSet());
    }
}
