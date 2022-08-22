package ru.practicum.shareit.requests.service;


import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.model.ItemRequest;

import java.util.List;

public interface ItemRequestService {

    ItemRequest findById(Long requestId, Long userId);

    ItemRequestDto findByIdDto(Long requestId, Long userId);

    ItemRequestDto create(long userId, ItemRequestDto itemRequestDto);

    List<ItemRequestDto> getByRequestor(long userId);

    List<ItemRequestDto> getAll(long userId, int from, int size);
}
