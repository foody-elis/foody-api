package com.example.foody.service.impl;

import com.example.foody.builder.SittingTimeBuilder;
import com.example.foody.dto.response.SittingTimeResponseDTO;
import com.example.foody.exceptions.entity.EntityCreationException;
import com.example.foody.exceptions.entity.EntityDeletionException;
import com.example.foody.exceptions.entity.EntityNotFoundException;
import com.example.foody.exceptions.sitting_time.InvalidWeekDayException;
import com.example.foody.mapper.SittingTimeMapper;
import com.example.foody.model.SittingTime;
import com.example.foody.model.WeekDayInfo;
import com.example.foody.repository.RestaurantRepository;
import com.example.foody.repository.SittingTimeRepository;
import com.example.foody.service.SittingTimeService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(rollbackOn = Exception.class)
public class SittingTimeServiceImpl implements SittingTimeService {
    private final SittingTimeRepository sittingTimeRepository;
    private final RestaurantRepository restaurantRepository;
    private final SittingTimeMapper sittingTimeMapper;
    private final SittingTimeBuilder sittingTimeBuilder;

    public SittingTimeServiceImpl(SittingTimeRepository sittingTimeRepository, RestaurantRepository restaurantRepository, SittingTimeMapper sittingTimeMapper, SittingTimeBuilder sittingTimeBuilder) {
        this.sittingTimeRepository = sittingTimeRepository;
        this.restaurantRepository = restaurantRepository;
        this.sittingTimeMapper = sittingTimeMapper;
        this.sittingTimeBuilder = sittingTimeBuilder;
    }

    @Override
    public List<SittingTime> createForWeekDayInfo(WeekDayInfo weekDayInfo) {
        List<SittingTime> sittingTimes = new ArrayList<>();

        int minutes = weekDayInfo.getSittingTimeStep().getMinutes();
        sittingTimes.addAll(generate(weekDayInfo, weekDayInfo.getStartLaunch(), weekDayInfo.getEndLaunch(), minutes));
        sittingTimes.addAll(generate(weekDayInfo, weekDayInfo.getStartDinner(), weekDayInfo.getEndDinner(), minutes));

        return sittingTimes;
    }

    private List<SittingTime> generate(WeekDayInfo weekDayInfo, LocalTime start, LocalTime end, int minutes) {
        List<SittingTime> sittingTimes = new ArrayList<>();
        LocalTime currentStart = start;
        LocalTime currentEnd;

        // Check if currentStart + minutes <= end
        while (currentStart.plusMinutes(minutes).isBefore(end) || currentStart.plusMinutes(minutes).equals(end)) {
            currentEnd = currentStart.plusMinutes(minutes);

            SittingTime sittingTime = sittingTimeBuilder
                    .start(currentStart)
                    .end(currentEnd)
                    .weekDayInfo(weekDayInfo)
                    .build();

            // Save the sitting time
            sittingTimes.add(save(sittingTime));

            currentStart = currentEnd;
        }

        return sittingTimes;
    }

    private SittingTime save(SittingTime sittingTime) {
        try {
            sittingTime = sittingTimeRepository.save(sittingTime);
        } catch (Exception e) {
            throw new EntityCreationException("sitting time");
        }

        return sittingTime;
    }

    @Override
    public List<SittingTimeResponseDTO> findAll() {
        List<SittingTime> sittingTimes = sittingTimeRepository.findAllByDeletedAtIsNull();
        return sittingTimeMapper.sittingTimesToSittingTimeResponseDTOs(sittingTimes);
    }

    @Override
    public List<SittingTimeResponseDTO> findAllByRestaurant(long restaurantId) {
        restaurantRepository
                .findByIdAndDeletedAtIsNullAndApproved(restaurantId, true)
                .orElseThrow(() -> new EntityNotFoundException("restaurant", "id", restaurantId));

        List<SittingTime> sittingTimes = sittingTimeRepository
                .findAllByDeletedAtIsNullAndWeekDayInfoRestaurantId(restaurantId);

        return sittingTimeMapper.sittingTimesToSittingTimeResponseDTOs(sittingTimes);
    }

    @Override
    public List<SittingTimeResponseDTO> findAllByRestaurantAndWeekDay(long restaurantId, int weekDay) {
        restaurantRepository
                .findByIdAndDeletedAtIsNullAndApproved(restaurantId, true)
                .orElseThrow(() -> new EntityNotFoundException("restaurant", "id", restaurantId));

        if (weekDay < 1 || weekDay > 7) throw new InvalidWeekDayException(weekDay);

        List<SittingTime> sittingTimes = sittingTimeRepository
                .findAllByDeletedAtIsNullAndRestaurantIdAndWeekDayOrderByStart(restaurantId, weekDay);

        return sittingTimeMapper.sittingTimesToSittingTimeResponseDTOs(sittingTimes);
    }

    @Override
    public List<SittingTimeResponseDTO> findAllByRestaurantAndWeekDayAndStartAfterNow(long restaurantId, int weekDay) {
        restaurantRepository
                .findByIdAndDeletedAtIsNullAndApproved(restaurantId, true)
                .orElseThrow(() -> new EntityNotFoundException("restaurant", "id", restaurantId));

        if (weekDay < 1 || weekDay > 7) throw new InvalidWeekDayException(weekDay);

        List<SittingTime> sittingTimes = sittingTimeRepository
                .findAllByDeletedAtIsNullAndRestaurantIdAndWeekDayAndStartAfterNowOrderByStart(restaurantId, weekDay);

        return sittingTimeMapper.sittingTimesToSittingTimeResponseDTOs(sittingTimes);
    }

    @Override
    public boolean remove(long id) {
        SittingTime sittingTime = sittingTimeRepository
                .findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new EntityNotFoundException("sitting time", "id", id));

        sittingTime.setDeletedAt(LocalDateTime.now());

        try {
            sittingTimeRepository.save(sittingTime);
        } catch (Exception e) {
            throw new EntityDeletionException("sitting time", "id", id);
        }

        return true;
    }
}
