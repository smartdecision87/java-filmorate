package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FilmorateApplicationTests {

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private FilmController filmController;

	@Autowired
	private UserController userController;

	@Test
	void contextLoads() {
		assertNotNull(filmController);
		assertNotNull(userController);
	}

	// Film Validation Tests
	@Test
	void shouldCreateFilmWhenAllFieldsAreValid() {
		Film film = createFilm();
		Film createdFilm = filmController.add(film);

		assertNotNull(createdFilm.getId());
		assertEquals(film.getName(), createdFilm.getName());
		assertEquals(film.getDescription(), createdFilm.getDescription());
		assertEquals(film.getReleaseDate(), createdFilm.getReleaseDate());
		assertEquals(film.getDuration(), createdFilm.getDuration());
	}

	@Test
	void shouldThrowExceptionWhenFilmReleaseDateIsBefore1895_12_28() {
		Film film = createFilm();
		film.setReleaseDate(LocalDate.of(1895, 12, 27));

		assertThrows(Exception.class, () -> filmController.add(film));
	}

	@Test
	void shouldAcceptFilmReleaseDateExactly1895_12_28() {
		Film film = createFilm();
		film.setReleaseDate(LocalDate.of(1895, 12, 28));

		Film createdFilm = filmController.add(film);
		assertEquals(film.getReleaseDate(), createdFilm.getReleaseDate());
	}

	// User Validation Tests
	@Test
	void shouldCreateUserWhenAllFieldsAreValid() {
		User user = createUser();
		User createdUser = userController.create(user);

		assertNotNull(createdUser.getId());
		assertEquals(user.getEmail(), createdUser.getEmail());
		assertEquals(user.getLogin(), createdUser.getLogin());
		assertEquals(user.getName(), createdUser.getName());
		assertEquals(user.getBirthday(), createdUser.getBirthday());
	}

	@Test
	void shouldSetLoginAsNameWhenUserNameIsBlank() {
		User user = createUser();
		user.setName("");

		User createdUser = userController.create(user);
		assertEquals(user.getLogin(), createdUser.getName());
	}

	@Test
	void shouldReturnNotFoundWhenUpdatingNonExistentFilm() {
		Film film = createFilm();
		film.setId(999L);

		assertThrows(Exception.class, () -> filmController.update(film));
	}

	@Test
	void shouldReturnNotFoundWhenUpdatingNonExistentUser() {
		User user = createUser();
		user.setId(999L);

		assertThrows(Exception.class, () -> userController.update(user));
	}

	private Film createFilm() {
		Film film = new Film();
		film.setName("Avatar 2");
		film.setDescription("It's a good film!");
		film.setReleaseDate(LocalDate.of(2000, 1, 1));
		film.setDuration(120);
		return film;
	}

	private User createUser() {
		User user = new User();
		user.setEmail("KevinClare99@gmail.com");
		user.setLogin("LocDog");
		user.setName("Kevin Clare");
		user.setBirthday(LocalDate.of(1991, 8, 9));
		return user;
	}
}
