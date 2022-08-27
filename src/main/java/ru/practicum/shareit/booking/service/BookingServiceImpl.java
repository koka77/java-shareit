package ru.practicum.shareit.booking.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingApproveDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.exceptions.*;
import ru.practicum.shareit.booking.mapper.BookingApproveMapper;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
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
    public List<BookingApproveDto> getBookingByCurrentOwner(long userId, String state, int from, int size) {
        PageRequest pageRequest = PageRequest.of(from / size, size, Sort.by(Sort.Direction.DESC, "start"));

        try {
            BookingStatus status = BookingStatus.valueOf(state);
            User owner = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
            List<Booking> bookingApproveDtos;

            switch (status) {
                case FUTURE:
                    bookingApproveDtos = bookingRepository.findAllByBookerInFuture(owner, pageRequest);
                    break;
                case WAITING:
                case REJECTED:
                case APPROVED:
                    bookingApproveDtos = bookingRepository.findAllByItemOwnerAndStatus(owner, status, pageRequest);
                    break;
                case CURRENT:
                    bookingApproveDtos = bookingRepository
                            .findAllByItemOwnerAndStartBeforeAndEndAfter(
                                    owner, LocalDateTime.now(), LocalDateTime.now(), pageRequest);
                    break;
                case PAST:
                    bookingApproveDtos = bookingRepository
                            .findAllByItemOwnerAndEndBefore(
                                    owner,
                                    LocalDateTime.now(),
                                    pageRequest
                            );
                    break;
                default:
                    bookingApproveDtos = bookingRepository.findAllByItemOwner(
                            owner,
                            pageRequest
                    );
            }

            return bookingApproveDtos
                    .stream()
                    .map(bookingApproveMapper::toBookingDto)
                    .sorted(Comparator.comparing(BookingApproveDto::getStart).reversed())
                    .collect(Collectors.toList());
        } catch (RuntimeException e) {
            throw new UnsupportedStatusException();
        }
    }

    @Override
    public List<BookingApproveDto> getBookingByCurrentUser(
            long userId,
            String state,
            int from,
            int size
    ) {

        PageRequest pageRequest = PageRequest.of(
                from / size,
                size,
                Sort.by(Sort.Direction.DESC, "start")
        );
        User booker = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        try {
            BookingStatus bookingStatus = BookingStatus.valueOf(state);


            List<Booking> list;
            switch (bookingStatus) {
                case ALL:
                    list = bookingRepository.findAllByBooker(booker, pageRequest);
                    break;
                case FUTURE:
                    list = bookingRepository.findAllByBookerInFuture(booker, pageRequest);
                    break;
                case CURRENT:
                    list = bookingRepository.findAllByBookerInCurrent(booker, pageRequest);
                    break;
                case PAST:
                    list = bookingRepository.findAllByBookerInPast(
                            booker,
                            LocalDateTime.now(),
                            pageRequest
                    );
                    break;
                case REJECTED:
                    list = bookingRepository.findAllByBookerAndStatus(
                            booker,
                            BookingStatus.REJECTED,
                            pageRequest
                    );
                    break;
                case WAITING:
                    list = bookingRepository.findAllByBookerAndStatus(
                            booker,
                            BookingStatus.WAITING,
                            pageRequest
                    );
                    break;
                default:
                    throw new UnsupportedStatusException();


            }

            List<BookingApproveDto> bookingApproveDtos = list.stream()
                    .map(bookingApproveMapper::toBookingDto)
                    .sorted(Comparator.comparing(BookingApproveDto::toString).reversed())
                    .collect(Collectors.toList());

            return bookingApproveDtos;
        } catch (RuntimeException e) {
            throw new UnsupportedStatusException();
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
