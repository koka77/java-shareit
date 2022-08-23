package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findAllByBooker(User booker, PageRequest pageRequest);

    List<Booking> findByStatusNotAndBookerAndItemIdAndStartLessThanEqual(
            BookingStatus status, User booker, Long itemId, LocalDateTime localDateTime
    );

    @Query(" select b from Booking b " +
            "where   b.start > CURRENT_DATE  order by b.id asc ")
    List<Booking> findAllByBookerInFuture(User booker, PageRequest pageRequest);

    @Query(" select b from Booking b " +
            "where  ?1 = b.booker and    b.end < ?2 order by b.id asc ")
    List<Booking> findAllByBookerInPast(User booker, LocalDateTime current, PageRequest pageRequest);

    @Query(" select b from Booking b " +
            "where  ?1 = b.booker and b.start <= CURRENT_TIMESTAMP and b.end >= CURRENT_TIMESTAMP order by b.id asc ")
    List<Booking> findAllByBookerInCurrent(User booker, PageRequest pageRequest);

    List<Booking> findAllByItemOwnerAndStartBeforeAndEndAfter(
            User owner, LocalDateTime current1, LocalDateTime current2, PageRequest pageRequest
    );

    List<Booking> findAllByItemOwnerAndEndBefore(User owner, LocalDateTime current, PageRequest pageRequest);

    List<Booking> findAllByBookerAndStatus(User booker, BookingStatus status, PageRequest pageRequest);

    List<Booking> findAllByItemOwnerAndStatus(User owner, BookingStatus status, PageRequest pageRequest);

    List<Booking> findAllByItemOwner(User owner, PageRequest pageRequest);

    @Query("select b from Booking b join fetch Item i on i.id = b.item.id" +
            " where i.id = ?1  and i.owner.id = ?2   order by b.start desc  ")
    List<Booking> next(Long id, Long ownerId);

    @Query(" select  b from Booking b join fetch Item i on i.id = b.item.id" +
            " where i.id = ?1 and i.owner.id = ?2 and b.status <>  'REJECTED' order by b.start asc  ")
    List<Booking> last(Long id, Long ownerId);
}
