package ru.practicum.shareit.item;

import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }


    @PostMapping
    public Optional<ItemDto> create(@NotBlank @RequestHeader("X-Sharer-User-Id") long userId,
                                    @Valid @RequestBody ItemDto itemDto) {
        return itemService.create(userId, itemDto);
    }

    @DeleteMapping("/{itemId}")
    public void deleteById(@PathVariable Long itemId) {
        itemService.deleteById(itemId);
    }

    @PatchMapping("/{itemId}")
    public Optional<ItemDto> update(@NotBlank @RequestHeader("X-Sharer-User-Id") long userId,
                                    @PathVariable Long itemId, @RequestBody ItemDto itemDto) {
        return itemService.updateById(userId, itemId, itemDto);
    }

    @GetMapping
    public Collection<ItemDto> findAll(@NotBlank @RequestHeader("X-Sharer-User-Id") long ownerId) {
        return itemService.findAll(ownerId);
    }

    @GetMapping("/{itemId}")
    public Optional<ItemDto> findById(@PathVariable Long itemId) {
        return itemService.findById(itemId);
    }

    @GetMapping("search")
    public Collection<ItemDto> search(@NotBlank @RequestHeader("X-Sharer-User-Id") long ownerId,
                                      @RequestParam(required = false) String text) {
        return itemService.searchItem(ownerId, text);
    }
}
