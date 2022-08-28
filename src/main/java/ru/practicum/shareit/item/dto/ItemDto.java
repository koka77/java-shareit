package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.requests.model.ItemRequest;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemDto {
    private Long id;
    @NotEmpty
    private String name;
    @NotEmpty
    private String description;

    @NotNull
    private Boolean available;

    Long requestId;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private ItemRequest request;

    BookingDto nextBooking;

    BookingDto lastBooking;

    List<CommentDto> comments;
}
