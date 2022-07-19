package ru.practicum.shareit.requests.repository;

import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.requests.ItemRequest;

import java.util.Collection;
import java.util.Optional;

public interface ItemRequestRepository {

    void create(ItemRequest request);

    void deleteById(Long id);

    void update(ItemRequest request);

    Collection<ItemRequest> findAll();

    Optional<ItemRequest> findById(Long id);
}
