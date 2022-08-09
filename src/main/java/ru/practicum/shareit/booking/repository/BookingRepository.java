package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findAllByBooker(User booker);

    @Query(" select b from Booking b " +
            "where  ?1 = b.booker and    b.start >= ?2 order by b.id asc ")
    List<Booking> findAllByBookerInFuture(User booker, LocalDateTime current);

    @Query(" select b from Booking b " +
            "where  ?1 = b.booker and    b.end < ?2 order by b.id asc ")
    List<Booking> findAllByBookerInPast(User booker, LocalDateTime current);

    @Query(" select b from Booking b " +
            "where  ?1 = b.booker and    b.start <= ?2 and b.end >= ?2 order by b.id asc ")
    List<Booking> findAllByBookerInCurrent(User booker, LocalDateTime current);

    List<Booking> findAllByItemOwner(User owner);

    List<Booking> findAllByItem(Item item);
}
