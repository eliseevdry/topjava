package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
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
        //в цикле создаем новые объекты по условию и добавляем их в результирующих список
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
        //создаем список дат, в которых превышены калории
        List<LocalDate> excessList = meals.stream()            //создание стрима
                .collect(Collectors.groupingBy(s -> LocalDate.from(s.getDateTime()), Collectors.summingInt(UserMeal::getCalories))) //разделение стрима на дни, в мапе указываем сумму калорий
                .entrySet().stream().filter(s -> s.getValue() > caloriesPerDay).map(Map.Entry::getKey).collect(Collectors.toList()); //загоняем обратно в стрим, фильтруем его по условию превышения калорий, ключи отправляем в лист

        return meals.stream()  //создаем стрим
                        .filter(s -> TimeUtil.isBetweenHalfOpen(LocalTime.from(s.getDateTime()), startTime, endTime))  //фильтрация по времени
                        .map(s -> excessList.contains(LocalDate.from(s.getDateTime())) ?
                                new UserMealWithExcess(s.getDateTime(), s.getDescription(), s.getCalories(), true) :
                                new UserMealWithExcess(s.getDateTime(), s.getDescription(), s.getCalories(), false)) //если дата блюда существует в excessList, то создаем объект с true, если нет с false
                .collect(Collectors.toList());
    }
}
