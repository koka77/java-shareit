package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.Collection;
import java.util.Optional;

public interface UserService {
    UserDto create(UserDto userDto);

    void deleteById(Long id);

    UserDto updateById(Long userId, UserDto userDto);

    Collection<UserDto> findAll();

    Optional<UserDto> findById(Long id);

}
