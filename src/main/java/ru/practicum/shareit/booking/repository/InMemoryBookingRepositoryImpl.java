package ru.practicum.shareit.booking.repository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.booking.model.Booking;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class InMemoryBookingRepositoryImpl implements BookingRepository {
    private static Long currentId = 1L;
    private final static Map<Long, Booking> BOOKING_MAP = new HashMap<>();

    @Override
    public Booking create(Booking booking) {
        if (BOOKING_MAP.containsValue(booking)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }

        booking.setId(currentId++);
        BOOKING_MAP.put(booking.getId(), booking);
        return Optional.of(booking).orElseThrow();
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
