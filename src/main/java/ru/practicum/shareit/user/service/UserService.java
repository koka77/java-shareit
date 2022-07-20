package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserService {
    Optional<UserDto> create(UserDto userDto);

    void deleteById(Long id);

    Optional<UserDto> updateById(Long userId, UserDto userDto);

    Collection<User> findAll();

    Optional<User> findById(Long id);

}
