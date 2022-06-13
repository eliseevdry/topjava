package ru.javawebinar.topjava.web.meal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;

import java.time.LocalDateTime;
import java.util.List;

import static ru.javawebinar.topjava.web.SecurityUtil.authUserId;

@Controller
public class MealRestController {
    @Autowired
    private MealService service;

    public Meal get(int id) {
        return service.get(authUserId(), id);
    }

    public void delete(int id) {
        service.delete(authUserId(), id);
    }

    public void update(Meal meal, Integer id) {
        service.update(authUserId(), meal, id);
    }


    public List<Meal> getAll() {
        return service.getAll(authUserId());
    }

    public List<Meal> getAllWithFilter(LocalDateTime start, LocalDateTime end) {
        return service.getAllWithFilter(authUserId(), start, end);
    }

}