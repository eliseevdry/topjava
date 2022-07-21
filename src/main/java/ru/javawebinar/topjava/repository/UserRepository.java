package ru.javawebinar.topjava.repository;

import org.springframework.validation.annotation.Validated;
import ru.javawebinar.topjava.model.User;

import javax.validation.Valid;
import java.util.List;

@Validated
public interface UserRepository {
    // null if not found, when updated
    User save(@Valid User user);

    // false if not found
    boolean delete(int id);

    // null if not found
    User get(int id);

    // null if not found
    User getByEmail(String email);

    List<User> getAll();

    default User getWithMeals(int id) {
        throw new UnsupportedOperationException();
    }
}