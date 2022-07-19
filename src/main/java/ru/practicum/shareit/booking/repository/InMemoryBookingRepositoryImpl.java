package ru.practicum.shareit.booking.repository;

import ru.practicum.shareit.booking.Booking;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryBookingRepositoryImpl implements BookingRepository {
    private static Long currentId = 1L;
    private final static Map<Long, Booking> BOOKING_MAP = new HashMap<>();

    @Override
    public void create(Booking booking) {
        booking.setId(currentId++);
        BOOKING_MAP.put(currentId, booking);
    }

    @Override
    public void deleteById(Long id) {
        BOOKING_MAP.remove(id);
    }

    @Override
    public void update(Booking booking) {
        BOOKING_MAP.put(booking.getId(), booking);

    }

    @Override
    public Collection<Booking> findAll() {
        return BOOKING_MAP.values();
    }

    @Override
    public Optional<Booking> findById(Long id) {
        return Optional.of(BOOKING_MAP.get(id));
    }
}
