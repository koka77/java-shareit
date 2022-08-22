package ru.practicum.shareit.requests.service;

import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.mapper.ItemRequestMapper;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.requests.repository.ItemRequestRepository;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestRepository itemRequestRepository;
    private final ItemRequestMapper itemRequestMapper;
    private final UserService userService;

    private final ItemService itemService;
    private final UserMapper userMapper;
    private final ItemMapper itemMapper;


    public ItemRequestServiceImpl(ItemRequestRepository itemRequestRepository,
                                  ItemRequestMapper itemRequestMapper,
                                  UserService userService,
                                  @Lazy ItemService itemService, UserMapper userMapper, ItemMapper itemMapper) {
        this.itemRequestRepository = itemRequestRepository;
        this.itemRequestMapper = itemRequestMapper;
        this.userService = userService;
        this.itemService = itemService;
        this.userMapper = userMapper;
        this.itemMapper = itemMapper;
    }


    @Override
    public ItemRequest findById(Long id) {
        return itemRequestRepository.findById(id).orElseThrow();
    }

    @Override
    public ItemRequestDto create(long userId, ItemRequestDto itemRequestDto) {
        ItemRequest itemRequest = itemRequestMapper.toItemRequest(itemRequestDto);
        itemRequest.setCreated(LocalDateTime.now());
        itemRequest.setRequestor(userMapper.toUser(userService.findById(userId).orElseThrow()));
        return itemRequestMapper.toItemRequestDto(itemRequestRepository.save(itemRequest));
    }

    @Override
    public List<ItemRequestDto> getByRequestor(long userId) {
        User requestor = userMapper.toUser(userService.findById(userId).orElseThrow());
        List<Item> items = itemService.findByRequestor(requestor);
        List<ItemDto> itemDtoList = items.stream().map(itemMapper::toItemDto).collect(Collectors.toList());

        List<ItemRequest> requests = itemRequestRepository.findByRequestorOrderByCreatedDesc(requestor);
        List<ItemRequestDto> dtoList = itemRequestMapper.toDtoList(requests);

        dtoList.stream().forEach(itemRequestDto -> itemRequestDto.setItems(itemDtoList));

        return dtoList;

    }

    @Override
    public List<ItemRequestDto> getAll(long userId, int from, int size) {
        User requestor = userMapper.toUser(userService.findById(userId).orElseThrow());
        List<ItemRequestDto> result = itemRequestRepository.findAllByRequestorIsNot(requestor,
                        PageRequest.of(from / size,
                                size,
                                Sort.by(Sort.Direction.DESC, "created"))
                )
                .stream()
                .map(itemRequestMapper::toItemRequestDto).peek(itemRequestDto -> {
                    if (itemRequestDto.getItems() == null) {
                        itemRequestDto.setItems(new ArrayList<>());
                    }
                }).collect(Collectors.toList());

        return result;
    }
}
