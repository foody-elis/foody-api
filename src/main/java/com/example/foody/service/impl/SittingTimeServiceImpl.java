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
import com.example.foody.repository.SittingTimeRepository;
import com.example.foody.service.BookingService;
import com.example.foody.service.SittingTimeService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(rollbackOn = Exception.class)
public class SittingTimeServiceImpl implements SittingTimeService {
    private final SittingTimeRepository sittingTimeRepository;
    private final SittingTimeMapper sittingTimeMapper;
    private final SittingTimeBuilder sittingTimeBuilder;
    private final BookingService bookingService;

    public SittingTimeServiceImpl(SittingTimeRepository sittingTimeRepository, SittingTimeMapper sittingTimeMapper, SittingTimeBuilder sittingTimeBuilder, BookingService bookingService) {
        this.sittingTimeRepository = sittingTimeRepository;
        this.sittingTimeMapper = sittingTimeMapper;
        this.sittingTimeBuilder = sittingTimeBuilder;
        this.bookingService = bookingService;
    }

    @Override
    public List<SittingTime> createForWeekDayInfo(WeekDayInfo weekDayInfo) {
        List<SittingTime> sittingTimes = new ArrayList<>();

        int minutes = weekDayInfo.getSittingTimeStep().getMinutes();
        sittingTimes.addAll(
                generateSittingTimes(weekDayInfo, weekDayInfo.getStartLaunch(), weekDayInfo.getEndLaunch(), minutes)
        );
        sittingTimes.addAll(
                generateSittingTimes(weekDayInfo, weekDayInfo.getStartDinner(), weekDayInfo.getEndDinner(), minutes)
        );

        return sittingTimes;
    }

    @Override
    public List<SittingTimeResponseDTO> findAll() {
        List<SittingTime> sittingTimes = sittingTimeRepository.findAll();
        return sittingTimeMapper.sittingTimesToSittingTimeResponseDTOs(sittingTimes);
    }

    @Override
    public List<SittingTimeResponseDTO> findAllByRestaurant(long restaurantId) {
        List<SittingTime> sittingTimes = sittingTimeRepository
                .findAllByWeekDayInfoRestaurantId(restaurantId);
        return sittingTimeMapper.sittingTimesToSittingTimeResponseDTOs(sittingTimes);
    }

    @Override
    public List<SittingTimeResponseDTO> findAllByRestaurantAndWeekDay(long restaurantId, int weekDay) {
        checkWeekDay(weekDay);
        List<SittingTime> sittingTimes = sittingTimeRepository
                .findAllByWeekDayInfo_Restaurant_IdAndWeekDayInfo_WeekDayOrderByStart(restaurantId, weekDay);
        return sittingTimeMapper.sittingTimesToSittingTimeResponseDTOs(sittingTimes);
    }

    @Override
    public List<SittingTimeResponseDTO> findAllByRestaurantAndWeekDayAndStartAfterNow(long restaurantId, int weekDay) {
        checkWeekDay(weekDay);
        List<SittingTime> sittingTimes = sittingTimeRepository
                .findAllByRestaurantIdAndWeekDayAndStartAfterNowOrderByStart(restaurantId, weekDay);
        return sittingTimeMapper.sittingTimesToSittingTimeResponseDTOs(sittingTimes);
    }

    @Override
    public boolean remove(long id) {
        SittingTime sittingTime = sittingTimeRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("sitting time", "id", id));
        sittingTime.delete();

        removeBookings(sittingTime);

        try {
            sittingTimeRepository.save(sittingTime);
        } catch (Exception e) {
            throw new EntityDeletionException("sitting time", "id", id);
        }

        return true;
    }

    private List<SittingTime> generateSittingTimes(WeekDayInfo weekDayInfo, LocalTime start, LocalTime end, int minutes) {
        if (start == null || end == null) return new ArrayList<>();

        List<SittingTime> sittingTimes = new ArrayList<>();
        LocalTime currentStart = start;

        while (!currentStart.isAfter(end.minusMinutes(minutes))) {
            LocalTime currentEnd = currentStart.plusMinutes(minutes);

            SittingTime sittingTime = sittingTimeBuilder
                    .start(currentStart)
                    .end(currentEnd)
                    .weekDayInfo(weekDayInfo)
                    .build();

            sittingTimes.add(saveSittingTime(sittingTime));

            currentStart = currentEnd;
        }

        return sittingTimes;
    }

    private SittingTime saveSittingTime(SittingTime sittingTime) {
        try {
            return sittingTimeRepository.save(sittingTime);
        } catch (Exception e) {
            throw new EntityCreationException("sitting time");
        }
    }

    private void checkWeekDay(int weekDay) {
        if (weekDay >= 1 && weekDay <= 7) return;

        throw new InvalidWeekDayException(weekDay);
    }

    private void removeBookings(SittingTime sittingTime) {
        sittingTime.getBookings().forEach(booking ->
                // Booking owner is notified by email about the deletion. This is why I don't delete bookings in SittingTime
                bookingService.remove(booking.getId())
        );
    }
}