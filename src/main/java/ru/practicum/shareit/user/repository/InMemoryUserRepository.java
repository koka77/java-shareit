package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.Optional;

public interface InMemoryUserRepository {

    User create(User user);

    void deleteById(Long id);

    User updateById(Long userId, User user);

    Collection<User> findAll();

    Optional<User> findById(Long id);
}
