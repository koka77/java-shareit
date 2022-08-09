package ru.practicum.shareit.booking.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

@Table(name = "BOOKINGS")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "DATE_START")
    private LocalDateTime start;


    @Column(name = "DATE_END")
    private LocalDateTime end;

    @ManyToOne
    @JoinColumn(name = "ID_ITEM", referencedColumnName = "id")

    @NonNull
    private Item item;

    @ManyToOne
    @JoinColumn(name = "ID_BOOKER", referencedColumnName = "id")
    private User booker;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    private BookingStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATE")
    private BookingState state = BookingState.ALL;
}
