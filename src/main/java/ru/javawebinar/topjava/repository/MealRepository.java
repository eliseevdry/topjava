package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.List;

public interface MealRepository {
    // null if updated meal does not belong to userId
    Meal save(int userId, Meal meal, Integer id);

    // false if meal does not belong to userId
    boolean delete(int userId, int id);

    // null if meal does not belong to userId
    Meal get(int userId, int id);

    List<Meal> getAll(int userId);

    List<Meal> getAllWithFilter(int userId, LocalDateTime start, LocalDateTime end);
}
