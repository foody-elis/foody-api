package com.example.foody.repository;

import com.example.foody.model.SittingTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface SittingTimeRepository extends JpaRepository<SittingTime, Long> {
    List<SittingTime> findAllByDeletedAtIsNull();

    Optional<SittingTime> findByIdAndDeletedAtIsNull(long id);

    @Query("select s from SittingTime s " +
            "where s.deletedAt is null and s.restaurant.id = :restaurantId " +
            "order by s.startTime")
    List<SittingTime> findAllByDeletedAtIsNullAndRestaurantOrderByStartTimeAsc(long restaurantId);

    @Query("select s from SittingTime s " +
            "where s.deletedAt is null and s.restaurant.id = :restaurantId and s.weekDay = :weekday and s.startTime > current_time " +
            "order by s.startTime")
    List<SittingTime> findAllByDeletedAtIsNullAndRestaurantAndWeekDayAndStartTimeAfterNowOrderByStartTimeAsc(long restaurantId, int weekday);

    @Query("select s from SittingTime s " +
            "where s.deletedAt is null and s.restaurant.id = :restaurantId and s.weekDay = :weekDay and " +
            "((s.startTime = :startTime and s.endTime = :startTime) or " +
            "(s.startTime = :endTime and s.endTime = :endTime) or " +
            "(s.startTime = :startTime and s.endTime = :endTime))")
    List<SittingTime> findAllWithOverlappingTime(long restaurantId, int weekDay, LocalTime startTime, LocalTime endTime);
}
