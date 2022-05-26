package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),

                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);

//        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        //создаем результирующий список, сразу устанавливаем максимально возможный размер, чтобы не тратить ресурсы на расширение
        List<UserMealWithExcess> result = new ArrayList<>(meals.size());
        //создаем мапу, которая будет содержать сумму калорий по дням
        Map<LocalDate, Integer> days = new HashMap<>();
        //проходимся по блюдам и суммируем калории в мапе
        for (UserMeal meal : meals) {
            LocalDate date = LocalDate.from(meal.getDateTime());
            int cal = meal.getCalories();
            if (days.containsKey(date)) {
                days.put(date, days.get(date) + cal);
            } else days.put(date, cal);
        }
        //сначала сделал в 3 цикла, потом решил объединить в 2, так как метод не перегружен и в него не получится зайти, не указав промежуток времени
//        for (UserMeal meal : meals) {
//            if (days.get(LocalDate.from(meal.getDateTime())) > caloriesPerDay
//                    && LocalTime.from(meal.getDateTime()).isAfter(startTime)
//                    && LocalTime.from(meal.getDateTime()).isBefore(endTime)) {
//                result.add(new UserMealWithExcess(meal.getDateTime(), meal.getDescription(), meal.getCalories(), true));
//            } else if (LocalTime.from(meal.getDateTime()).isAfter(startTime)
//                    && LocalTime.from(meal.getDateTime()).isBefore(endTime))
//                result.add(new UserMealWithExcess(meal.getDateTime(), meal.getDescription(), meal.getCalories(), false));
//        }
        for (UserMeal meal : meals) {
            if (days.get(LocalDate.from(meal.getDateTime())) > caloriesPerDay
                    && TimeUtil.isBetweenHalfOpen(LocalTime.from(meal.getDateTime()), startTime, endTime)) {
                result.add(new UserMealWithExcess(meal.getDateTime(), meal.getDescription(), meal.getCalories(), true));
            } else if (TimeUtil.isBetweenHalfOpen(LocalTime.from(meal.getDateTime()), startTime, endTime))
                result.add(new UserMealWithExcess(meal.getDateTime(), meal.getDescription(), meal.getCalories(), false));
        }
        return result;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
//        List<UserMealWithExcess> result = meals.stream().





        return null;
    }
}
