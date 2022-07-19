package ru.practicum.shareit.requests.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import ru.practicum.shareit.requests.ItemRequest;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.user.mapper.UserMapper;

@Mapper(componentModel = "spring", uses = UserMapper.class, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface ItemRequestMapper {

    ItemRequestDto toItemRequestDto(ItemRequest request);

    ItemRequest toItemRequest(ItemRequestDto dto);
}
