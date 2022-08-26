package ru.practicum.shareit.booking.mapper;

import org.mapstruct.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.user.mapper.UserMapper;

@Mapper(componentModel = "spring", uses = {UserMapper.class, ItemMapper.class},
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface BookingMapper {

    @Mapping(source = "item.id", target = "itemId")
    @Mapping(source = "item.status", target = "status")
    @Mapping(source = "booker.id", target = "bookerId")
    @Mapping(source = "id", target = "id")
    BookingDto toBookingDto(Booking item);

    Booking toBooking(BookingDto dto);

    void updateBookingFromDto(BookingDto dto, @MappingTarget Booking booking);
}
