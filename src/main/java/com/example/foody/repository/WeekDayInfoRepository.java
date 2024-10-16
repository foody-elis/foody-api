package com.example.foody.repository;

import com.example.foody.model.WeekDayInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WeekDayInfoRepository extends JpaRepository<WeekDayInfo, Long> {
    boolean existsByDeletedAtIsNullAndWeekDayAndRestaurantId(int weekDay, long restaurantId);
    List<WeekDayInfo> findAllByDeletedAtIsNull();
    Optional<WeekDayInfo> findByIdAndDeletedAtIsNull(long id);
}
