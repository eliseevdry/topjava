package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.AbstractNamedEntity;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class InMemoryUserRepository implements UserRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryUserRepository.class);
    private final Map<Integer, User> repository = new HashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        save(new User("EliseevAndrey", "eliseevdry@mail.ru", "admin1", Role.ADMIN));
        save(new User("IvanovIvan", "ivanov@gmail.ru", "12345678", Role.USER));
        save(new User("SemenovSemen", "semenov@gmail.ru", "87654321", Role.USER));
        save(new User("SidorovaIrina", "sidorova@gmail.ru", "978675645342", Role.USER));
        save(new User("PetrovaElena", "petrova@gmail.ru", "elena19", Role.USER));
    }

    @Override
    public boolean delete(int id) {
        log.info("delete {}", id);
        return repository.remove(id) != null;
    }

    @Override
    public User save(User user) {
        log.info("save {}", user);
        if (user.isNew()) {
            user.setId(counter.incrementAndGet());
            repository.put(user.getId(), user);
            return user;
        }
        return repository.computeIfPresent(user.getId(), (id, oldUser) -> user);
    }

    @Override
    public User get(int id) {
        log.info("get {}", id);
        return repository.get(id);
    }

    @Override
    public List<User> getAll() {
        log.info("getAll");
        List<User> users = new ArrayList<>(repository.values());
        users.sort(Comparator.comparing(AbstractNamedEntity::getName));
        return users;
    }

    @Override
    public User getByEmail(String email) {
        log.info("getByEmail {}", email);
        for (User u : getAll()) {
            if (u.getEmail().equals(email)) {
                return u;
            }
        }
        return null;
    }
}
