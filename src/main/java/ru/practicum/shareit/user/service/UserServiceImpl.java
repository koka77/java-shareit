package ru.practicum.shareit.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collection;
import java.util.Optional;

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
    public Optional<UserDto> create(UserDto userDto) {
        User user = userMapper.toUser(userDto);
        return Optional.of(userMapper.toUserDto(userRepository.create(user).get()));
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public Optional<UserDto> updateById(Long userId, UserDto userDto) {

        if (userRepository.findAll().stream().anyMatch(s-> s.getEmail().equals(userDto.getEmail()))){
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }

        User user =  userRepository.findById(userId).orElseThrow();

        userMapper.updateUserFromDto(userDto, user);
        return Optional.of(userMapper.toUserDto(userRepository.updateById(userId, user)));
    }

    @Override
    public Collection<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }
}
