package com.example.foody.repository;

import com.example.foody.model.WeekDayInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WeekDayInfoRepository extends JpaRepository<WeekDayInfo, Long> {
    List<WeekDayInfo> findAllByRestaurantIdOrderByWeekDay(long restaurantId);
}