package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class MealServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(MealServlet.class);

    private List<Meal> meals;
    private static final int CALORIES_PER_DAY = 2000;

    @Override
    public void init() throws ServletException {
        meals = new CopyOnWriteArrayList<>();
        meals.addAll(Arrays.asList(
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        ));
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<MealTo> mealsTo = MealsUtil.filteredByStreams(meals, LocalTime.of(0, 0), LocalTime.of(23, 59, 59, 9999), CALORIES_PER_DAY);
        request.setAttribute("mealsTo", mealsTo);
        log.debug("forward to meals");
        request.getRequestDispatcher("meals.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF8");

        if (!requestIsValid(request)) {
            doGet(request, response);
        }

        final String date = request.getParameter("date");
        final String description = request.getParameter("description");
        final String calories = request.getParameter("calories");

        final Meal meal = new Meal(LocalDateTime.parse(date), description, Integer.parseInt(calories));

        meals.add(meal);

        doGet(request, response);
    }

    private boolean requestIsValid(final HttpServletRequest request) {
        final String date = request.getParameter("date");
        final String description = request.getParameter("description");
        final String calories = request.getParameter("calories");

        return date != null && date.length() > 0 &&
                description != null && description.length() > 0 &&
                calories != null && calories.length() > 0;
    }
}
