package com.example.foody.repository;

import com.example.foody.model.SittingTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SittingTimeRepository extends JpaRepository<SittingTime, Long> {
    List<SittingTime> findAllByDeletedAtIsNull();

    Optional<SittingTime> findByIdAndDeletedAtIsNull(long id);

    @Query("select s from SittingTime s " +
            "where s.deletedAt is null and s.weekDayInfo.restaurant.id = :restaurantId and s.weekDayInfo.weekDay = :weekDay " +
            "order by s.start")
    List<SittingTime> findAllByDeletedAtIsNullAndRestaurantIdAndWeekDayOrderByStart(long restaurantId, int weekDay);

    @Query("select s from SittingTime s " +
            "where s.deletedAt is null and s.weekDayInfo.restaurant.id = :restaurantId and s.weekDayInfo.weekDay = :weekDay and s.start > current_timestamp " +
            "order by s.start")
    List<SittingTime> findAllByDeletedAtIsNullAndRestaurantIdAndWeekDayAndStartAfterNowOrderByStart(long restaurantId, int weekDay);
}
