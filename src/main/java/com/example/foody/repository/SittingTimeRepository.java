package com.example.foody.repository;

import com.example.foody.model.SittingTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SittingTimeRepository extends JpaRepository<SittingTime, Long> {
    List<SittingTime> findAllByWeekDayInfoRestaurantId(long restaurantId);

    List<SittingTime> findAllByWeekDayInfo_Restaurant_IdAndWeekDayInfo_WeekDayOrderByStart(long restaurantId, int weekDay);

    @Query("""
            SELECT s
            FROM SittingTime s
            WHERE s.weekDayInfo.restaurant.id = :restaurantId
            AND s.weekDayInfo.weekDay = :weekDay
            AND s.start > current_timestamp
            ORDER BY s.start
            """)
    List<SittingTime> findAllByRestaurantIdAndWeekDayAndStartAfterNowOrderByStart(long restaurantId, int weekDay);

    @Query("""
            SELECT s
            FROM SittingTime s
            WHERE s.weekDayInfo.restaurant.id = :restaurantId
            AND s.weekDayInfo.weekDay = :weekDay
            AND s.start > current_timestamp
            ORDER BY s.start
            LIMIT :limit
            """)
    List<SittingTime> findAllByRestaurant_IdAndWeekDayAndStartAfterNowOrderByStartLimit(long restaurantId, int weekDay, int limit);
}