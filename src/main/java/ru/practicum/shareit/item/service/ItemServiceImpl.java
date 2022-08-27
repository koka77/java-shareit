package ru.practicum.shareit.item.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.exceptions.ItemNotFoundException;
import ru.practicum.shareit.item.exceptions.UserHasNotPermission;
import ru.practicum.shareit.item.exceptions.UserNotBookerException;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.requests.service.ItemRequestService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {

    private final ItemMapper itemMapper;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    private final CommentMapper commentMapper;
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;

    private final ItemRequestService itemRequestService;


    @Autowired
    public ItemServiceImpl(ItemMapper itemMapper, ItemRepository itemRepository, UserRepository userRepository, CommentMapper commentMapper, CommentRepository commentRepository, BookingRepository bookingRepository, BookingMapper bookingMapper, ItemRequestService itemRequestService) {
        this.itemMapper = itemMapper;
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
        this.commentMapper = commentMapper;
        this.commentRepository = commentRepository;
        this.bookingRepository = bookingRepository;
        this.bookingMapper = bookingMapper;
        this.itemRequestService = itemRequestService;
    }

    @Override
    public ItemDto create(long userId, ItemDto itemDto) {

        Item item = itemMapper.toItem(itemDto);
        item.setOwner(userRepository.findById(userId).orElseThrow());
        if (itemDto.getRequestId() != null)
            item.setRequest(itemRequestService.findById(itemDto.getRequestId(), userId));
        item = itemRepository.save(item);
        ItemDto dto = itemMapper.toItemDto(item);
        return dto;
    }

    @Override
    public ItemDto updateById(Long userId, Long id, ItemDto itemDto) {

        User user = userRepository.findById(userId).orElseThrow(() -> new UserHasNotPermission());
        Item result = itemRepository.findById(id).orElseThrow(() -> new ItemNotFoundException(id));
        if (!result.getOwner().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        itemMapper.updateItemFromDto(itemDto, result);

        result = itemRepository.save(result);
        return itemMapper.toItemDto(result);
    }

    @Override
    public Collection<ItemDto> findAll(Long ownerId) {

        return itemRepository.findAll().stream().filter(item -> item.getOwner().getId().equals(ownerId))
                .map(itemMapper::toItemDto).sorted(Comparator.comparing(ItemDto::getId)).peek(itemDto -> {
                    itemDto.setNextBooking(bookingRepository
                            .next(itemDto.getId(), ownerId)
                            .isEmpty() ? null : bookingMapper.toBookingDto(bookingRepository
                            .next(itemDto.getId(), ownerId).get(0)));
                    itemDto.setLastBooking(bookingRepository.last(itemDto.getId(), ownerId)
                            .isEmpty() ? null : bookingMapper.toBookingDto(bookingRepository
                            .last(itemDto.getId(), ownerId).get(0)));
                })
                .collect(Collectors.toSet());
    }

    @Override
    public ItemDto findById(Long id, Long ownerId) {


        Item item = itemRepository.findById(id).orElseThrow(() -> new ItemNotFoundException(id));
        ItemDto dto = itemMapper.toItemDto(item);

        Booking nextBooking = bookingRepository.next(id, ownerId).isEmpty() ? null : bookingRepository.next(id, ownerId).get(0);
        Booking lastBooking = bookingRepository.last(id, ownerId).isEmpty() ? null : bookingRepository.last(id, ownerId).get(0);
        dto.setLastBooking(bookingMapper.toBookingDto(lastBooking));
        dto.setNextBooking(bookingMapper.toBookingDto(nextBooking));

        return setComments(dto);
    }

    @Override
    public Collection<ItemDto> search(String text) {
        if (text.isEmpty()) {
            return new ArrayList<>();
        }
        return itemRepository.search(text).stream()
                .map(itemMapper::toItemDto)
                .map(this::setComments)
                .collect(Collectors.toList());
    }

    @Override
    public CommentDto createComment(long userId, long itemId, CommentDto commentDto) {

        User user = userRepository.findById(userId).orElseThrow();
        if (bookingRepository.findByStatusNotAndBookerAndItemIdAndStartLessThanEqual(BookingStatus.REJECTED, user, itemId, LocalDateTime.now()).isEmpty()) {
            throw new UserNotBookerException();
        }

        Item item = itemRepository.findById(itemId).orElseThrow();
        Comment comment = commentMapper.toComment(commentDto);
        comment.setItem(item);
        comment.setAuthor(user);
        comment.setCreated(LocalDateTime.now());
        return commentMapper.toCommentDto(commentRepository.save(comment));
    }

    @Override
    public List<Item> findByRequestor(User requestor) {

        return itemRepository.findByRequestRequestor(requestor);
    }

    @Override
    public List<Item> findByRequestId(Long id) {
        return itemRepository.findByRequestId(id);
    }

    public List<Comment> findCommentsByItem(long itemId) {
        return commentRepository.findCommentsByItem(itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemNotFoundException(itemId)));
    }

    public ItemDto setComments(ItemDto itemdto) {
        List<CommentDto> comments = commentMapper.toDtoList(findCommentsByItem(itemdto.getId()));

        itemdto.setComments(comments);

        return itemdto;
    }
}
