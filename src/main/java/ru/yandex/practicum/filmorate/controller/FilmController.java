package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {
    private final Map<Long, Film> films = new HashMap<>();
    private long id;

    // добавление фильма
    @PostMapping
    public Film add(@Valid @RequestBody Film film) throws ValidationException {
        checkReleaseDate(film.getReleaseDate());
        log.info("Добавляем фильм " + film.getName());
        film.setId(getNextId());
        films.put(film.getId(), film);
        return film;
    }

    // обновление фильма
    @PutMapping
    public Film update(@Valid @RequestBody Film updatedFilm) {
        if (updatedFilm.getId() == null) {
            log.warn("Отсутствует идентификатор фильма " + updatedFilm.getName());
            throw new ValidationException("Отсутствует идентификатор фильма.");
        }
        checkReleaseDate(updatedFilm.getReleaseDate());
        Film existedFilm = films.get(updatedFilm.getId());
        if (existedFilm == null) {
            log.warn("Фильм с данным идентификатором отсутствует.");
            throw new NotFoundException("Фильм с данным идентификатором отсутствует.");
        }
        log.info("Обновляем данные о фильме " + updatedFilm.getName());
        films.put(updatedFilm.getId(), updatedFilm);
        return updatedFilm;
    }

    @GetMapping
    public List<Film> getList() {
        log.info("Получаем список всех фильмов.");
        return films.values().stream().toList();
    }

    private long getNextId() {
        return ++id;
    }

    private void checkReleaseDate(LocalDate releaseDate) {
        if (releaseDate.isBefore(LocalDate.of(1895, 12, 28))) {
            log.warn("Дата не должна быть раннее 28 декабря 1895 года.");
            throw new ValidationException("Дата не должна быть раннее 28 декабря 1895 года.");
        }
    }
}
