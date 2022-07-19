package ru.practicum.shareit.item;

import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.Collection;
import java.util.Optional;

/**
 * // TODO .
 */
@RestController
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }


    @PostMapping
    public Optional<ItemDto> create(@RequestBody ItemDto itemDto) {
        return itemService.create(itemDto);
    }

    @DeleteMapping("/{itemId}")
    public void deleteById(@PathVariable Long userId) {
        itemService.deleteById(userId);
    }

    @PatchMapping("/{itemId}")
    public Optional<ItemDto> update(@PathVariable Long itemId, @RequestBody ItemDto itemDto) {
        return itemService.updateById(itemId, itemDto);
    }

    @GetMapping
    public Collection<ItemDto> findAll() {
        return itemService.findAll();
    }

    @GetMapping("/{itemId}")
    public Optional<ItemDto> findById(@PathVariable Long itemId) {
        return itemService.findById(itemId);
    }
}
