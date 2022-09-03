package ru.practicum.shareit.requests;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.mapper.ItemRequestMapper;
import ru.practicum.shareit.requests.service.ItemRequestService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Validated
@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {

    private final ItemRequestService itemRequestService;
    private final ItemRequestMapper itemRequestMapper;

    @Autowired
    public ItemRequestController(ItemRequestService itemRequestService, ItemRequestMapper itemRequestMapper) {
        this.itemRequestService = itemRequestService;
        this.itemRequestMapper = itemRequestMapper;
    }

    @PostMapping()
    public ItemRequestDto create(@Min(1) @RequestHeader("X-Sharer-User-Id") long userId,
                                 @RequestBody @Valid ItemRequestDto itemRequestDto) {
        return itemRequestService.create(userId, itemRequestDto);
    }

    @GetMapping()
    List<ItemRequestDto> getByRequestor(@Min(1) @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemRequestService.getByRequestor(userId);
    }

    @GetMapping("/all")
    List<ItemRequestDto> getAll(@Min(1) @RequestHeader("X-Sharer-User-Id") long userId,
                                @RequestParam(defaultValue = "1") @PositiveOrZero int from,
                                @RequestParam(defaultValue = "10") int size) {

        return itemRequestService.getAll(userId, from, size);
    }

    @GetMapping("/{requestId}")
    ItemRequestDto getById(@Min(1) @RequestHeader("X-Sharer-User-Id") long userId,
                           @PathVariable long requestId) {
        return itemRequestService.findByIdDto(requestId, userId);
    }
}
