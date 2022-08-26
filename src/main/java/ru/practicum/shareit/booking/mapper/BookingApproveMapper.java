package ru.practicum.shareit.booking.mapper;

import org.mapstruct.*;
import ru.practicum.shareit.booking.dto.BookingApproveDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.user.mapper.UserMapper;

@Mapper(componentModel = "spring", uses = {UserMapper.class, ItemMapper.class},
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface BookingApproveMapper {

    @Mapping(source = "item.id", target = "itemId")
    @Mapping(source = "item.status", target = "status")
    @Mapping(target = "booker", source = "booker", defaultExpression = "java(userMapper.toUserDto(item.getBooker()))")
    @Mapping(source = "id", target = "id")
    @Mapping(source = "item", target = "item", defaultExpression = "java(itemMapper.toItemDto(item.getItem()))")
    BookingApproveDto toBookingDto(Booking item);

    Booking toBooking(BookingApproveDto dto);

    void updateBookingFromDto(BookingApproveDto dto, @MappingTarget Booking booking);
}
