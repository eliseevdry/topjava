package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MealDao<T extends Meal> implements Dao<T>{
    Map<Integer, T> keeper = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    @Override
    public T save(T t) {
        if (t.isNewMeal()) {
            t.setId(counter.incrementAndGet());
        }
        return keeper.put(t.getId(), t);
    }

    @Override
    public void delete(int i) {
        keeper.remove(i);
    }

    @Override
    public T get(int i) {
        return keeper.get(i);
    }

    @Override
    public Collection<T> getAll() {
        return keeper.values();
    }
}
