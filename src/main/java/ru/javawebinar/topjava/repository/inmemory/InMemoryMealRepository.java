package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.util.DateTimeUtil.isBetweenHalfOpenDate;
import static ru.javawebinar.topjava.util.DateTimeUtil.isBetweenHalfOpenTime;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private final Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.meals.forEach(m -> save(1, m, null));
    }

    @Override
    public Meal save(int userId, Meal meal, Integer id) {
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            meal.setUserId(userId);
            Map<Integer, Meal> subMap = repository.get(userId);
            if (subMap == null) {
                subMap = new ConcurrentHashMap<>();
            }
            subMap.put(meal.getId(), meal);
            repository.put(userId, subMap);
            return meal;
        } else {
            return repository.get(userId).put(id, meal);
        }
    }

    @Override
    public boolean delete(int userId, int id) {
        return get(userId, id) != null && repository.get(userId).remove(id) != null;
    }

    @Override
    public Meal get(int userId, int id) {
        return repository.get(userId).get(id);
    }


    @Override
    public List<Meal> getAll(int userId) {
        return filterByPredicate(userId, meal -> true, meal -> true);
    }

    @Override
    public List<Meal> getAllWithFilter(int userId, LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime) {
        return filterByPredicate(userId,
                meal -> isBetweenHalfOpenDate(meal.getDateTime(), startDate, endDate),
                meal -> isBetweenHalfOpenTime(meal.getDateTime(), startTime, endTime));
    }

    private List<Meal> filterByPredicate(int userId, Predicate<Meal> filterDate, Predicate<Meal> filterTime) {
        return repository.get(userId).values().stream()
                .filter(filterDate)
                .filter(filterTime)
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }
}

