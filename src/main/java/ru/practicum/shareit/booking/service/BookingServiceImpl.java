package ru.practicum.shareit.booking.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingApproveDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.exceptions.*;
import ru.practicum.shareit.booking.mapper.BookingApproveMapper;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BookingMapper bookingMapper;
    private final BookingApproveMapper bookingApproveMapper;

    public BookingServiceImpl(BookingRepository bookingRepository, UserRepository userRepository, ItemRepository itemRepository, BookingMapper bookingMapper, BookingApproveMapper bookingApproveMapper) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
        this.bookingMapper = bookingMapper;
        this.bookingApproveMapper = bookingApproveMapper;
    }

    @Override
    public BookingDto create(Long userId, BookingDto dto) {

        if (userRepository.findById(userId).isEmpty()) {
            throw new NoUserException();
        }


        Item item = itemRepository.findById(dto.getItemId())
                .orElseThrow(() -> new ItemNotFoundException("не найден предмет"));

        if (item.getOwner().getId().equals(userId)) {
            throw new UserHasNotPermission();
        }
        if (!item.getAvailable()) {
            throw new ItemNotAvailableException();
        }

        Booking booking = bookingMapper.toBooking(dto);
        booking.setItem(item);

        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        booking.setStatus(BookingStatus.WAITING);
        booking.setBooker(user);

        booking = bookingRepository.save(booking);
        dto = bookingMapper.toBookingDto(booking);
        return dto;

    }

    @Override
    public List<BookingDto> getBookingByCurrentOwner(long userId, BookingState state) {
        User owner = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        return bookingRepository.findAllByBookerInFuture(owner)
                .stream()
                .map(bookingMapper::toBookingDto)
                .filter(bookingDtoState -> bookingDtoState.getState().equals(state))
                .sorted(Comparator.comparing(BookingDto::getState).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingApproveDto> getBookingByCurrentUser(long userId, String state) {

            User booker = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        try {
            BookingState bookingState = BookingState.valueOf(state);


            List<Booking> list = new ArrayList<>();
            switch (bookingState) {
                case ALL:
                    list = bookingRepository.findAllByBooker(booker);
                    break;
                case FUTURE:
                    list = bookingRepository.findAllByBookerInFuture(booker, LocalDateTime.now());
                    break;
                case CURRENT:
                    list = bookingRepository.findAllByBookerInCurrent(booker, LocalDateTime.now());
                    break;
                case PAST:
                    list = bookingRepository.findAllByBookerInPast(booker, LocalDateTime.now());
                    break;
                case REJECTED:
                    list = bookingRepository.findAllByBookerInCurrent(booker, LocalDateTime.now());
                    break;
                case WAITING:
                    list = bookingRepository.findAllByBookerInCurrent(booker, LocalDateTime.now());
                    break;
                default:
                    throw new UnsupportedStatusException("Unknown state: " + state);


            }

            return list
                    .stream()
                    .map(bookingApproveMapper::toBookingDto)
                    .sorted(Comparator.comparing(BookingApproveDto::toString).reversed())
                    .collect(Collectors.toList());
        } catch (RuntimeException e) {
            throw new UnsupportedStatusException("sadsa");
        }
    }

    @Override
    public BookingApproveDto approveBooking(Long userId, Long bookingId, boolean approved) {
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
        return bookingApproveMapper.toBookingDto(bookingRepository.save(booking));
    }

    @Override
    public BookingApproveDto getBookingById(Long userId, Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException("booking not found"));
        if (!userId.equals(booking.getItem().getOwner().getId()) && !userId.equals(booking.getBooker().getId())) {
            throw new UserHasNotPermission();
        }
        return bookingApproveMapper.toBookingDto(booking);
    }
}
