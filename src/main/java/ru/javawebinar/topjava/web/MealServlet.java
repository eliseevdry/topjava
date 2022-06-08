package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.dao.Dao;
import ru.javawebinar.topjava.dao.MealDao;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;

public class MealServlet extends HttpServlet {
    private static final Logger LOG = LoggerFactory.getLogger(MealServlet.class);

    private Dao<Meal> dao;
    private static final int CALORIES_PER_DAY = 2000;

    @Override
    public void init() throws ServletException {
        super.init();
        dao = new MealDao<>();
        dao.save(new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500));
        dao.save(new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000));
        dao.save(new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500));
        dao.save(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100));
        dao.save(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000));
        dao.save(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500));
        dao.save(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410));
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOG.info("**********************************GET**************************************");
        String action = request.getParameter("action");

        if (action == null) {
            request.setAttribute("mealsTo", MealsUtil.filteredByStreams(dao.getAll(), LocalTime.MIN, LocalTime.MAX, CALORIES_PER_DAY));
            request.getRequestDispatcher("/meals.jsp").forward(request, response);
        } else if (action.equals("delete")) {
            int id = Integer.parseInt(request.getParameter("id"));
            LOG.info(request.getParameter("id"));
            dao.delete(id);
            response.sendRedirect("meals");
        } else {
            final Meal meal;
            if (action.equals("create")) {
                meal = new Meal();
            } else {
                meal = dao.get(Integer.parseInt(request.getParameter("id")));
                request.setAttribute("id", request.getParameter("id"));
                request.setAttribute("description", request.getParameter("description"));
                request.setAttribute("date", request.getParameter("dateTime"));
                request.setAttribute("calories", request.getParameter("calories"));
            }
            request.setAttribute("meal", meal);
            request.getRequestDispatcher("/addMeal.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOG.info("**********************************POST**************************************");
        request.setCharacterEncoding("UTF8");
        String idS = request.getParameter("id");
        Integer id = null;
        if (!idS.isEmpty()) {
            id = Integer.parseInt(idS);
        }
        Meal meal = new Meal(id, LocalDateTime.parse(request.getParameter("date")), request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));
        dao.save(meal);
        response.sendRedirect("meals");
    }
}
