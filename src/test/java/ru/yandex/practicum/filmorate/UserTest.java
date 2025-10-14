package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

public class UserTest {
    private User user;
    @Autowired
    private UserController userController;

    @BeforeEach
    void init() {
        user = new User();
        user.setEmail("KevinClare99@gmail.com");
        user.setLogin("LocDog");
        user.setName("Kevin Clare");
        user.setBirthday(LocalDate.of(1991, 8, 9));
    }

    @Test
    void shouldCreateUserWhenAllFieldsAreValid() {
        User createdUser = userController.create(user);
        assertNotNull(createdUser.getId());
        assertEquals(user.getEmail(), createdUser.getEmail());
        assertEquals(user.getLogin(), createdUser.getLogin());
        assertEquals(user.getName(), createdUser.getName());
        assertEquals(user.getBirthday(), createdUser.getBirthday());
    }

    @Test
    void shouldSetLoginAsNameWhenUserNameIsBlank() {
        user.setName("");
        User createdUser = userController.create(user);
        assertEquals(user.getLogin(), createdUser.getName());
    }

    @Test
    void shouldReturnNotFoundWhenUpdatingNonExistentUser() {
        user.setId(999L);
        assertThrows(Exception.class, () -> userController.update(user));
    }
}
