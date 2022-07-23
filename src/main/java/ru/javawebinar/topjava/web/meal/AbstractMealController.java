package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserCaloriesPerDay;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserId;

@Component
public abstract class AbstractMealController {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    protected MealService service;

    public Meal get(int id) {
        log.info("get meal {} for user {}", id, authUserId());
        return service.get(id, authUserId());
    }

    public List<MealTo> getAll() {
        log.info("getAll for user {}", authUserId());
        return MealsUtil.getTos(service.getAll(authUserId()), authUserCaloriesPerDay());
    }

    public void delete(int id) {
        log.info("delete meal {} for user {}", id, authUserId());
        service.delete(id, authUserId());
    }

    public Meal create(Meal meal) {
        checkNew(meal);
        log.info("create {} for user {}", meal, authUserId());
        return service.create(meal, authUserId());
    }

    public void update(Meal meal, int id) {
        assureIdConsistent(meal, id);
        log.info("update {} for user {}", meal, authUserId());
        service.update(meal, authUserId());
    }

    /**
     * <ol>Filter separately
     * <li>by date</li>
     * <li>by time for every date</li>
     * </ol>
     */
    public List<MealTo> getBetween(@Nullable LocalDate startDate, @Nullable LocalTime startTime,
                                   @Nullable LocalDate endDate, @Nullable LocalTime endTime) {
        log.info("getBetween dates({} - {}) time({} - {}) for user {}", startDate, endDate, startTime, endTime, authUserId());

        List<Meal> mealsDateFiltered = service.getBetweenInclusive(startDate, endDate, authUserId());
        return MealsUtil.getFilteredTos(mealsDateFiltered, authUserCaloriesPerDay(), startTime, endTime);
    }
}
