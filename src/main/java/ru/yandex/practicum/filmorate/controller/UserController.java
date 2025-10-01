package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {
    private Map<Long, User> users = new HashMap<>();
    private long id;

    // создание пользователя
    @PostMapping
    public User create(@Valid @RequestBody User user) throws ValidationException {
        if (user == null) {
            throw new ValidationException("Нет данных.");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        user.setId(getNextId());
        log.info(String.format("Добавляем пользователя %s", user.getName()));
        users.put(user.getId(), user);

        return user;
    }

    // обновление пользователя
    @PutMapping
    public User update(@Valid @RequestBody User updatedUser) throws ValidationException {
        if (!users.containsKey(updatedUser.getId())) {
            log.warn("Пользователь " + updatedUser.getName() + " не найден!");
            throw new NotFoundException("Пользователь не найден!");
        }
        User existUser = users.get(updatedUser.getId());
        if (updatedUser.getLogin().isBlank()) {
            existUser.setLogin(updatedUser.getEmail());
        }
        existUser.setName(updatedUser.getName());
        existUser.setEmail(updatedUser.getEmail());
        existUser.setBirthday(updatedUser.getBirthday());
        log.info("Обновляем данные о пользователе " + existUser.getName());
        users.put(existUser.getId(), existUser);
        return updatedUser;
    }

    // получение всех пользователей
    @GetMapping
    public List<User> getAllUsers() {
        return users.values().stream().toList();
    }

    private long getNextId() {
        return ++id;
    }
}
