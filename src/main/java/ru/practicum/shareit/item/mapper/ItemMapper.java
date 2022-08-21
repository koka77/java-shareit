package ru.practicum.shareit.item.mapper;

import org.mapstruct.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.mapper.ItemRequestMapper;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.user.mapper.UserMapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {UserMapper.class, ItemRequestMapper.class},
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ItemMapper {

    @Mapping(source="request.id",target="requestId")
    ItemDto toItemDto(Item item);

    @Mapping(target = "owner", ignore = true)
    Item toItem(ItemDto dto);

    void updateItemFromDto(ItemDto dto, @MappingTarget Item item);
/*
    @Mapping(target = "List<ItemDto>")
    List<ItemDto> toDtoList(List<Item> list);*/

}
