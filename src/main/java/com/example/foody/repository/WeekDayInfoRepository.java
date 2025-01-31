package com.example.foody.repository;

import com.example.foody.model.WeekDayInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository interface for managing {@link WeekDayInfo} entities.
 * <p>
 * Extends the {@link JpaRepository} interface to provide CRUD operations for {@link WeekDayInfo} entities.
 */
public interface WeekDayInfoRepository extends JpaRepository<WeekDayInfo, Long> {

    /**
     * Finds all WeekDayInfo entities by the restaurant ID, ordered by the weekday.
     *
     * @param restaurantId the ID of the restaurant
     * @return a list of WeekDayInfo entities associated with the specified restaurant, ordered by the weekday
     */
    List<WeekDayInfo> findAllByRestaurantIdOrderByWeekDay(long restaurantId);
}