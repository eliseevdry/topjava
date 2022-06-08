package ru.javawebinar.topjava.dao;

import java.util.Collection;

public interface Dao<T> {
    T save(T t);

    void delete(int i);

    T get(int i);

    Collection<T> getAll();
}
