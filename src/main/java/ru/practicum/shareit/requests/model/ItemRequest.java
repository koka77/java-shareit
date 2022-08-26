package ru.practicum.shareit.requests.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Table(name = "REQUESTS")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_REQUESTOR")
    private User requestor;
/*
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable (
            name = "ITEM_REQUEST",
            joinColumns = @JoinColumn (name = "ID_REQUEST"),
            inverseJoinColumns = @JoinColumn(name = "ID_ITEM")
    )
    private List<Item> items = new ArrayList<>();*/

    private LocalDateTime created;
}
