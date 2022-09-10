package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingDtoItem;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemDto {
    long id;

    private String name;

    private String description;

    private Boolean available;

    private Long requestId;

    private BookingDtoItem nextBooking;

    private BookingDtoItem lastBooking;

    private List<CommentDto> comments;
}
