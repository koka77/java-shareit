package ru.practicum.shareit.requests;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.exception.InvalidPaginationException;
import ru.practicum.shareit.requests.service.ItemRequestService;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * // TODO .
 */
@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {

    private final ItemRequestService itemRequestService;

    @Autowired
    public ItemRequestController(ItemRequestService itemRequestService) {
        this.itemRequestService = itemRequestService;
    }

    @PostMapping()
    ItemRequestDto create(@NotBlank @RequestHeader("X-Sharer-User-Id") long userId,
                          @RequestBody @Valid ItemRequestDto itemRequestDto) {
        return itemRequestService.create(userId, itemRequestDto);
    }

    @GetMapping()
    List<ItemRequestDto> getByRequestor(@NotBlank @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemRequestService.getByRequestor(userId);
    }

   @GetMapping("/all")
    List<ItemRequestDto> getAll(@NotBlank @RequestHeader("X-Sharer-User-Id") long userId,
                                @Valid @RequestParam(defaultValue = "1") int from,
                                @RequestParam(defaultValue = "10") int size) {
        if (from < 0) {
            throw new InvalidPaginationException();
        }
        return itemRequestService.getAll(userId, from, size);
    }
/*
    @GetMapping("/{requestId}")
    ItemRequestDto getById(@NotBlank @RequestHeader("X-Sharer-User-Id") long userId,
                           @PathVariable long requestId) {
        return itemRequestService.getById(userId, requestId);
    }*/
}
