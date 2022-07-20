package ru.practicum.shareit.requests.repository;

import ru.practicum.shareit.requests.ItemRequest;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryItemRequestRepositoryImpl implements ItemRequestRepository {
    private static Long currentId = 1L;
    private final static Map<Long, ItemRequest> ITEM_REQUEST_MAP = new HashMap<>();

    @Override
    public void create(ItemRequest request) {
        request.setId(currentId++);
        ITEM_REQUEST_MAP.put(currentId, request);
    }

    @Override
    public void deleteById(Long id) {
        ITEM_REQUEST_MAP.remove(id);
    }

    @Override
    public void update(ItemRequest request) {
        ITEM_REQUEST_MAP.put(request.getId(), request);

    }

    @Override
    public Collection<ItemRequest> findAll() {
        return ITEM_REQUEST_MAP.values();
    }

    @Override
    public Optional<ItemRequest> findById(Long id) {
        return Optional.of(ITEM_REQUEST_MAP.get(id));
    }
}
