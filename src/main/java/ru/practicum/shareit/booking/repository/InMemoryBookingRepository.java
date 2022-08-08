package ru.practicum.shareit.booking.repository;

import ru.practicum.shareit.booking.model.Booking;

import java.util.Collection;
import java.util.Optional;

public interface InMemoryBookingRepository {

    Booking create(Booking booking);

    void deleteById(Long id);

    void update(Booking booking);

    Collection<Booking> findAll();

    Optional<Booking> findById(Long id);
}
