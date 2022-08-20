package ru.practicum.shareit.user.repository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class InMemoryUserRepositoryImpl implements InMemoryUserRepository {
    private static Long currentId = 1L;
    private final static Map<Long, User> USER_MAP = new HashMap<>();

    private final UserMapper userMapper;

    public InMemoryUserRepositoryImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public User create(User user) {
        if (USER_MAP.containsKey(user.getId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
        user.setId(currentId++);
        USER_MAP.put(user.getId(), user);
        return Optional.of(user).orElseThrow();
    }

    @Override
    public void deleteById(Long id) {
        USER_MAP.remove(id);
    }

    @Override
    public User updateById(Long userId, User user) {
        if (!USER_MAP.containsKey(userId)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
        User existUser = USER_MAP.get(userId);
        userMapper.updateUserFromDto(userMapper.toUserDto(user), existUser);
        USER_MAP.put(user.getId(), existUser);
        return USER_MAP.get(userId);

    }

    @Override
    public Collection<User> findAll() {
        return USER_MAP.values();
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.of(USER_MAP.get(id));
    }
}
