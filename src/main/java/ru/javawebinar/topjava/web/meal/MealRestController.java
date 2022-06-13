package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserId;

@Controller
public class MealRestController {
    protected final Logger log = LoggerFactory.getLogger(getClass());
    @Autowired
    private MealService service;

    public Meal get(int id) {
        log.info("get {} meal for user {}", id, authUserId());
        return service.get(authUserId(), id);
    }

    public void delete(int id) {
        log.info("delete {} meal for user {}", id, authUserId());
        service.delete(authUserId(), id);
    }

    public void update(Meal meal, Integer id) {
        log.info("update {} with id={} for user {}", meal, id, authUserId());
        assureIdConsistent(meal, id);
        service.update(authUserId(), meal, id);
    }

    public Meal create(Meal meal) {
        log.info("create {} meal for user {}", meal, authUserId());
        checkNew(meal);
        return service.create(authUserId(), meal);
    }


    public List<MealTo> getAll() {
        log.info("getAll for {}", authUserId());
        return service.getAll(authUserId());
    }

    public List<Meal> getAllWithFilter(LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime) {
        log.info("getAllWithFilter for user {}", authUserId());
        return service.getAllWithFilter(authUserId(), startDate, startTime, endDate, endTime);
    }

}