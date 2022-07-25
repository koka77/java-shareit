package ru.practicum.shareit.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserMapper userMapper, UserRepository userRepository) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
    }

    @Override
    public UserDto create(UserDto userDto) {
        if (userRepository.findAll().stream().anyMatch(s -> s.getEmail().equals(userDto.getEmail()))) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
        User user = userMapper.toUser(userDto);
        return userMapper.toUserDto(userRepository.create(user));
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public UserDto updateById(Long userId, UserDto userDto) {

        if (userRepository.findAll().stream().anyMatch(s -> s.getEmail().equals(userDto.getEmail()))) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }

        User user = userRepository.findById(userId).orElseThrow();

        userMapper.updateUserFromDto(userDto, user);
        return userMapper.toUserDto(userRepository.updateById(userId, user));
    }

    @Override
    public Collection<UserDto> findAll() {
        return userRepository.findAll().stream().map(userMapper::toUserDto).collect(Collectors.toList());
    }

    @Override
    public Optional<UserDto> findById(Long id) {
        Optional<User> user = userRepository.findById(id);
        return Optional.of(userMapper.toUserDto(user.get()));
    }
}
