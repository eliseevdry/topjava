package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
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
        MealsUtil.meals.forEach(m -> save(2, m));
        save(1, new Meal(LocalDateTime.of(2020, Month.JANUARY, 12, 12, 0), "ОБЕД АДМИНА", 1000));
        save(1, new Meal(LocalDateTime.of(2020, Month.JANUARY, 12, 20, 0), "УЖИН АДМИНА", 1000));
    }

    @Override
    public Meal save(int userId, Meal meal) {
        Integer id = meal.getId();

        if (meal.isNew()) {
            id = counter.incrementAndGet();
            meal.setId(id);
        } else if (get(id, userId) == null) {
            return null;
        }
        repository.computeIfAbsent(userId, ConcurrentHashMap::new);
        repository.get(userId).put(id, meal);
        return meal;
    }

    @Override
    public boolean delete(int userId, int id) {
        Map<Integer, Meal> subMap = repository.get(userId);
        return subMap != null && subMap.remove(id) != null;
    }

    @Override
    public Meal get(int userId, int id) {
        Map<Integer, Meal> subMap = repository.get(userId);
        return subMap == null ? null : subMap.get(id);
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

