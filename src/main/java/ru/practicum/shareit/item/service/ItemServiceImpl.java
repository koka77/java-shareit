package ru.practicum.shareit.item.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.exceptions.ItemNotFoundException;
import ru.practicum.shareit.item.exceptions.UserHasNotPermission;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {

    private final ItemMapper itemMapper;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    private final CommentMapper commentMapper;
    private final CommentRepository commentRepository;

    @Autowired
    public ItemServiceImpl(ItemMapper itemMapper, ItemRepository itemRepository, UserRepository userRepository, CommentMapper commentMapper, CommentRepository commentRepository) {
        this.itemMapper = itemMapper;
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
        this.commentMapper = commentMapper;
        this.commentRepository = commentRepository;
    }

    @Override
    public ItemDto create(long userId, ItemDto itemDto) {

        Item item = itemMapper.toItem(itemDto);
        item.setOwner(userRepository.findById(userId).orElseThrow());
        item = itemRepository.save(item);
        ItemDto dto = itemMapper.toItemDto(item);
        return dto;
    }

    @Override
    public void deleteById(Long id) {
        itemRepository.deleteById(id);
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
                .map(itemMapper::toItemDto).collect(Collectors.toSet());
    }

    @Override
    public ItemDto findById(Long id) {
        ItemDto dto = itemMapper.toItemDto(itemRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException(id)));
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
        return null;
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
