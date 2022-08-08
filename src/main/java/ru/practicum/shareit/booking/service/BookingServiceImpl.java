package ru.practicum.shareit.booking.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.exceptions.BookingNotChangeStatusException;
import ru.practicum.shareit.booking.exceptions.BookingNotFoundException;
import ru.practicum.shareit.booking.exceptions.NoUserException;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.exceptions.ItemNotFoundException;
import ru.practicum.shareit.item.exceptions.UserHasNotPermission;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BookingMapper bookingMapper;

    public BookingServiceImpl(BookingRepository bookingRepository, UserRepository userRepository, ItemRepository itemRepository, BookingMapper bookingMapper) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
        this.bookingMapper = bookingMapper;
    }

    @Override
    public BookingDto create(Long userId, BookingDto dto) {

        if (userRepository.findById(userId).isEmpty()){
            throw new NoUserException();
        }


        Item item = itemRepository.findById(dto.getItem().getId())
                .orElseThrow(() -> new ItemNotFoundException("не найден предмет"));
        if (item.getOwner().getId().equals(userId)) {
            throw new UserHasNotPermission();
        }

        Booking booking = bookingMapper.toBooking(dto);
        booking.setItem(item);

        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        booking.setStatus(BookingStatus.WAITING);

        return bookingMapper.toBookingDto(bookingRepository.save(booking));

    }

    @Override
    public List<BookingDto> getBookingByCurrentOwner(long userId, BookingState state) {
        return null;
    }

    @Override
    public List<BookingDto> getBookingByCurrentUser(long userId, BookingState state) {
        User booker = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        return bookingRepository.findAllByBooker(booker)
                .stream()
                .map(bookingMapper::toBookingDto)
                .filter(bookingDtoState -> bookingDtoState.getState().equals(state))
                .sorted(Comparator.comparing(BookingDto::getStart).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public BookingDto approveBooking(Long userId, Long bookingId, boolean approved) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException("booking not found"));
        if (!(booking.getStatus().equals(BookingStatus.WAITING))) {
            throw new BookingNotChangeStatusException("Невозможно изменить статус");
        }
        if (!userId.equals(booking.getItem().getOwner().getId())) {
            throw new UserHasNotPermission();
        }
        if (approved) {
            booking.setStatus(BookingStatus.APPROVED);
        } else {
            booking.setStatus(BookingStatus.REJECTED);
        }
        return bookingMapper.toBookingDto(bookingRepository.save(booking));
    }

    @Override
    public BookingDto getBookingById(Long userId, Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException("booking not found"));
        if (!userId.equals(booking.getItem().getOwner().getId()) && !userId.equals(booking.getBooker().getId())) {
            throw new UserHasNotPermission();
        }
        return bookingMapper.toBookingDto(booking);
    }
}
