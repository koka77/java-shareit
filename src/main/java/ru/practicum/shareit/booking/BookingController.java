package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingApproveDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.exceptions.ErrorResponse;
import ru.practicum.shareit.booking.exceptions.UnsupportedStatusException;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@Validated
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
                             @RequestBody @Valid BookingDto bookingDto) {

        return bookingService.create(userId, bookingDto);
    }


    @PatchMapping("/{bookingId}")
    BookingApproveDto approveStatus(@Min(1) @RequestHeader("X-Sharer-User-Id") long userId,
                                    @PathVariable Long bookingId,
                                    @RequestParam boolean approved) {
        return bookingService.approveBooking(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    BookingApproveDto getBookingById(@Min(1) @RequestHeader("X-Sharer-User-Id") long userId,
                                     @PathVariable long bookingId) {
        return bookingService.getBookingById(userId, bookingId);
    }

    @GetMapping()
    List<BookingApproveDto> getBookingCurrentUser(@Min(1) @RequestHeader("X-Sharer-User-Id") long userId,
                                                  @RequestParam(defaultValue = "ALL") String state,
                                                  @Min(1) @RequestParam(defaultValue = "1") int from,
                                                  @RequestParam(defaultValue = "10") int size) {
        List<BookingApproveDto> res = bookingService.getBookingByCurrentUser(userId, state, from, size);
        return res;
    }

    @GetMapping("/owner")
    List<BookingApproveDto> getBookingCurrentOwner(@Min(1) @RequestHeader("X-Sharer-User-Id") long userId,
                                                   @RequestParam(defaultValue = "ALL") String state,
                                                   @Min(1) @RequestParam(defaultValue = "1") int from,
                                                   @RequestParam(defaultValue = "10") int size) {
        return bookingService.getBookingByCurrentOwner(userId, state, from, size);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorResponse handleIncorrectParameterException(UnsupportedStatusException e) {
        return new ErrorResponse(HttpStatus.BAD_REQUEST, "Unknown state: UNSUPPORTED_STATUS");
//        return new ErrorResponse("Unknown state: UNSUPPORTED_STATUS");
    }

}