package ru.practicum.shareit.item.repository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class InMemoryItemRepositoryImpl implements ItemRepository {
    private static Long currentId = 1L;
    private final static Map<Long, Item> ITEM_MAP = new HashMap<>();

    @Override
    public Optional<Item> create(Item item) {
        if (ITEM_MAP.containsValue(item)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }

        item.setId(currentId++);
        ITEM_MAP.put(item.getId(), item);
        return Optional.of(item);
    }

    @Override
    public void deleteById(Long id) {
        ITEM_MAP.remove(id);
    }

    @Override
    public void update(Item item) {
        ITEM_MAP.put(item.getId(), item);

    }

    @Override
    public Collection<Item> findAll() {
        return ITEM_MAP.values();
    }

    @Override
    public Optional<Item> findById(Long id) {
        return Optional.of(ITEM_MAP.get(id));
    }
}
