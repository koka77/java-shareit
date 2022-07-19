package ru.practicum.shareit.booking.repository;

import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.user.User;

import java.util.Collection;
import java.util.Optional;

public interface BookingRepository {

    void create(Booking booking);

    void deleteById(Long id);

    void update(Booking booking);

    Collection<Booking> findAll();

    Optional<Booking> findById(Long id);
}
