package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Table(name = "COMMENTS")
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String text;

    @ManyToOne
    @JoinColumn(name = "ID_ITEM", referencedColumnName = "id")
    private Item item;

    @ManyToOne
    @JoinColumn(name = "ID_AUTHOR", referencedColumnName = "id")
    private User author;

    private LocalDateTime created;
}
