package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;

import java.time.LocalDateTime;
import java.util.List;

@Transactional(readOnly = true)
public interface CrudMealRepository extends JpaRepository<Meal, Integer> {

    @Transactional
    @Modifying
    @Query("DELETE FROM Meal m WHERE m.id=:id AND m.user.id=:userId")
    int delete(@Param("id") int id, @Param("userId") int userId);

    Meal findByIdAndUser(int id, User user);

    List<Meal> findByUserOrderByDateTimeDesc(User user);

    List<Meal> queryGetBetween(@Param("startDateTime") LocalDateTime startDateTime,
                               @Param("endDateTime") LocalDateTime endDateTime,
                               @Param("userId") int userId);

    @Query("SELECT meal FROM Meal meal JOIN FETCH meal.user user WHERE meal.id=:id AND meal.user.id=:userId")
    Meal getWithUser(@Param("id") int id, @Param("userId") int userId);
}

