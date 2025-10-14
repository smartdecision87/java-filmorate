package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

public class FilmTest {
    private Film film;
    @Autowired
    private FilmController filmController;

    @BeforeEach
    void init() {
        film = new Film();
        film.setName("Avatar 2");
        film.setDescription("It's a good film!");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(120);
    }

    @Test
    void shouldCreateFilmWhenAllFieldsAreValid() {
        Film createdFilm = filmController.add(film);
        assertNotNull(createdFilm.getId());
        assertEquals(film.getName(), createdFilm.getName());
        assertEquals(film.getDescription(), createdFilm.getDescription());
        assertEquals(film.getReleaseDate(), createdFilm.getReleaseDate());
        assertEquals(film.getDuration(), createdFilm.getDuration());
    }

    @Test
    void shouldThrowExceptionWhenFilmReleaseDateIsBefore1895_12_28() {
        film.setReleaseDate(LocalDate.of(1895, 12, 27));
        assertThrows(Exception.class, () -> filmController.add(film));
    }

    @Test
    void shouldAcceptFilmReleaseDateExactly1895_12_28() {
        film.setReleaseDate(LocalDate.of(1895, 12, 28));
        Film createdFilm = filmController.add(film);
        assertEquals(film.getReleaseDate(), createdFilm.getReleaseDate());
    }

    @Test
    void shouldReturnNotFoundWhenUpdatingNonExistentFilm() {
        film.setId(999L);
        assertThrows(Exception.class, () -> filmController.update(film));
    }
}
