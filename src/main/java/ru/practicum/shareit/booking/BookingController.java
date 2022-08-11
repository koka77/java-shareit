package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingApproveDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.exceptions.ErrorResponse;
import ru.practicum.shareit.booking.exceptions.UnsupportedStatusException;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
public class BookingController {

    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping()
    public BookingDto create(@RequestHeader("X-Sharer-User-Id") long userId,
                             @RequestBody  @Valid BookingDto bookingDto) {

        return bookingService.create(userId, bookingDto);
    }


    @PatchMapping("/{bookingId}")
    BookingApproveDto approveStatus(@NotBlank @RequestHeader("X-Sharer-User-Id") long userId,
                                        @PathVariable Long bookingId,
                                        @RequestParam boolean approved) {
        return bookingService.approveBooking(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    BookingApproveDto getBookingById(@NotBlank @RequestHeader("X-Sharer-User-Id") long userId,
                                    @PathVariable long bookingId) {
        return bookingService.getBookingById(userId, bookingId);
    }

    @GetMapping()
    List<BookingApproveDto> getBookingCurrentUser(@NotBlank @RequestHeader("X-Sharer-User-Id") long userId,
                                                @RequestParam(defaultValue = "ALL") String state) {
        List<BookingApproveDto> res = bookingService.getBookingByCurrentUser(userId, state);
        return res;
    }

    @GetMapping("/owner")
    List<BookingApproveDto> getBookingCurrentOwner(@NotBlank @RequestHeader("X-Sharer-User-Id") long userId,
                                                 @RequestParam(defaultValue = "ALL") String state) {
        return bookingService.getBookingByCurrentOwner(userId, state);
    }
    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorResponse handleIncorrectParameterException(UnsupportedStatusException e) {
        return new ErrorResponse(HttpStatus.BAD_REQUEST, "Unknown state: UNSUPPORTED_STATUS");
//        return new ErrorResponse("Unknown state: UNSUPPORTED_STATUS");
    }

}