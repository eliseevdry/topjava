package ru.javawebinar.topjava.repository.jpa;

import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class JpaMealRepository implements MealRepository {
    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public Meal save(Meal meal, int userId) {
        if (meal.isNew()) {
            User userRef = em.getReference(User.class, userId);
            meal.setUser(userRef);
            em.persist(meal);
        } else if (em.createNamedQuery(Meal.UPDATE)
                .setParameter("id", meal.getId())
                .setParameter("description", meal.getDescription())
                .setParameter("calories", meal.getCalories())
                .setParameter("date_time", meal.getDateTime())
                .setParameter("user_id", userId)
                .executeUpdate() == 0) {
            throw new NotFoundException("Not found with id " + meal.getId() + " and userId " + userId);
        }
        return meal;
    }

    @Override
    @Transactional
    public boolean delete(int id, int userId) {
        return em.createNamedQuery(Meal.DELETE)
                .setParameter("id", id)
                .setParameter("userId", userId)
                .executeUpdate() != 0;
    }

    @Override
    public Meal get(int id, int userId) {
        Meal meal = em.find(Meal.class, id);
        if (meal == null || meal.getUser().getId() != userId) {
            throw new NotFoundException("Not found with id " + id + " and userId " + userId);
        }
//        List<Meal> meals = em.createNamedQuery(Meal.BY_ID, Meal.class)
//                .setParameter(1, id)
//                .setParameter(2, userId)
//                .getResultList();
//        return DataAccessUtils.singleResult(meals);
        return meal;
    }

    @Override
    public List<Meal> getAll(int userId) {
        return em.createNamedQuery(Meal.ALL_SORTED, Meal.class)
                .setParameter(1, userId)
                .getResultList();
    }

    @Override
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        return em.createNamedQuery(Meal.BETWEEN_HALF_OPEN, Meal.class)
                .setParameter("userId", userId)
                .setParameter("startDateTime", startDateTime)
                .setParameter("endDateTime", endDateTime)
                .getResultList();
    }
}