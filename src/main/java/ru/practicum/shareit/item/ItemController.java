package ru.practicum.shareit.item;

import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.Collection;

@RestController
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }


    @PostMapping
    public ItemDto create(@NotBlank @RequestHeader("X-Sharer-User-Id") long userId,
                          @Valid @RequestBody ItemDto itemDto) {
        return itemService.create(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@NotBlank @RequestHeader("X-Sharer-User-Id") long userId,
                          @PathVariable Long itemId, @RequestBody ItemDto itemDto) {

        return itemService.updateById(userId, itemId, itemDto);
    }

    @GetMapping
    public Collection<ItemDto> findAll(@NotBlank @RequestHeader("X-Sharer-User-Id") long ownerId) {
        return itemService.findAll(ownerId);
    }

    @GetMapping("/{itemId}")
    public ItemDto findById(@PathVariable Long itemId, @NotBlank @RequestHeader("X-Sharer-User-Id") long ownerId) {
        return itemService.findById(itemId, ownerId);
    }

    @GetMapping("search")
    public Collection<ItemDto> search(@NotBlank @RequestHeader("X-Sharer-User-Id")
                                      @RequestParam(required = false) String text) {
        return itemService.search(text);
    }

    @PostMapping("/{itemId}/comment")
    CommentDto createComment(@NotBlank @RequestHeader("X-Sharer-User-Id") long userId,
                             @PathVariable long itemId,
                             @RequestBody @Valid CommentDto commentDto) {

        return itemService.createComment(userId, itemId, commentDto);
    }
}
