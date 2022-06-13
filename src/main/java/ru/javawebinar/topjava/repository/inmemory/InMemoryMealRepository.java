package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private final Map<Integer, Meal> repository = new HashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.meals.forEach(m->save(m.getUserId(), m));
    }

    @Override
    //TODO реализация сохранения/обновления не учитывает userId, не пойму как учесть
    public Meal save(int userId, Meal meal) {
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            repository.put(meal.getId(), meal);
            return meal;
        }
        // handle case: update, but not present in storage
        return repository.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int userId, int id) {
        if (get(userId, id) != null) {
            return repository.remove(id) != null;
        }
        return false;
    }

    @Override
    public Meal get(int userId, int id) {
        Meal meal = repository.get(id);
        if (meal.getUserId() == userId) {
            return meal;
        }
        return null;
    }

    @Override
    public List<Meal> getAll() {
        List <Meal> mealList = new ArrayList<>(repository.values());
        mealList.sort(Comparator.comparing(Meal::getDate));
        if (mealList.isEmpty()) {
            throw new NotFoundException("Repository is empty!");
        }
        return mealList;
    }

    @Override
    //TODO stream, один метод на всех, если userId=0, то выдать всю еду, атомарная реализация
    public List<Meal> getAllByUser(int userId) {
        List<Meal> mealList = new ArrayList<>();
        for (Meal meal : repository.values()) {
            if (meal.getUserId() == userId) {
                mealList.add(meal);
            }
        }
        if (mealList.isEmpty()) {
            throw new NotFoundException("Repository is empty!");
        }
        mealList.sort(Comparator.comparing(Meal::getDate));
        return mealList;
    }

    @Override
    public List<Meal> getAllWithFilter(int userId, LocalDateTime start, LocalDateTime end) {
        return getAllByUser(userId).stream().filter(meal -> DateTimeUtil.isBetweenHalfOpen(meal.getDateTime(),
                start.toLocalTime(), end.toLocalTime())).collect(Collectors.toList());
    }
}

