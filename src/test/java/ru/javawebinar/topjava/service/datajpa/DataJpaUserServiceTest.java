package ru.javawebinar.topjava.service.datajpa;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.AbstractUserServiceTest;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.util.List;

import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.UserTestData.*;


@ActiveProfiles(profiles = Profiles.DATAJPA)
public class DataJpaUserServiceTest extends AbstractUserServiceTest {
    @Test
    public void getWithMeals() {
        User user = new User(UserTestData.user);
        User actual = service.getWithMeals(USER_ID);
        MealTestData.MEAL_MATCHER.assertMatch(actual.getMeals(), MealTestData.meals);
        USER_MATCHER.assertMatch(actual, user);
    }

    @Test
    public void getWithMealsNotFound() {
        assertThrows(NotFoundException.class, () -> service.getWithMeals(NOT_FOUND));
    }

    @Test
    public void getWithoutMeals() {
        User user = new User(UserTestData.guest);
        User actual = service.getWithMeals(GUEST_ID);
        MealTestData.MEAL_MATCHER.assertMatch(actual.getMeals(), List.of());
        USER_MATCHER.assertMatch(actual, user);
    }
}