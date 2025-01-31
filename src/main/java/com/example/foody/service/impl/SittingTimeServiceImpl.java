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
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the SittingTimeService interface.
 * <p>
 * Provides methods to create, retrieve, and delete {@link SittingTime} objects.
 */
@Service
@Transactional(rollbackOn = Exception.class)
@AllArgsConstructor
public class SittingTimeServiceImpl implements SittingTimeService {

    private final SittingTimeRepository sittingTimeRepository;
    private final SittingTimeMapper sittingTimeMapper;
    private final SittingTimeBuilder sittingTimeBuilder;
    private final BookingService bookingService;

    /**
     * {@inheritDoc}
     * <p>
     * This method creates {@link SittingTime} entities for a given week day information.
     *
     * @param weekDayInfo the week day information
     * @return the list of created sitting times
     */
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

    /**
     * {@inheritDoc}
     * <p>
     * This method creates a {@link SittingTime}.
     *
     * @return the list of sitting time response data transfer objects
     */
    @Override
    public List<SittingTimeResponseDTO> findAll() {
        List<SittingTime> sittingTimes = sittingTimeRepository.findAll();
        return sittingTimeMapper.sittingTimesToSittingTimeResponseDTOs(sittingTimes);
    }

    /**
     * {@inheritDoc}
     * <p>
     * This method retrieves all {@link SittingTime} entities for a given restaurant.
     *
     * @param restaurantId the restaurant ID
     * @return the list of sitting time response data transfer objects
     */
    @Override
    public List<SittingTimeResponseDTO> findAllByRestaurant(long restaurantId) {
        List<SittingTime> sittingTimes = sittingTimeRepository
                .findAllByWeekDayInfoRestaurantId(restaurantId);
        return sittingTimeMapper.sittingTimesToSittingTimeResponseDTOs(sittingTimes);
    }

    /**
     * {@inheritDoc}
     * <p>
     * This method retrieves a {@link SittingTime} by its ID.
     *
     * @param restaurantId the restaurant ID
     * @param weekDay      the week day
     * @return the list of sitting time response data transfer objects
     */
    @Override
    public List<SittingTimeResponseDTO> findAllByRestaurantAndWeekDay(long restaurantId, int weekDay) {
        checkWeekDay(weekDay);
        List<SittingTime> sittingTimes = sittingTimeRepository
                .findAllByWeekDayInfo_Restaurant_IdAndWeekDayInfo_WeekDayOrderByStart(restaurantId, weekDay);
        return sittingTimeMapper.sittingTimesToSittingTimeResponseDTOs(sittingTimes);
    }

    /**
     * {@inheritDoc}
     * <p>
     * This method retrieves all {@link SittingTime} entities for a given restaurant and week day.
     *
     * @param restaurantId the restaurant ID
     * @param weekDay      the week day
     * @return the list of sitting time response data transfer objects
     */
    @Override
    public List<SittingTimeResponseDTO> findAllByRestaurantAndWeekDayAndStartAfterNow(long restaurantId, int weekDay) {
        checkWeekDay(weekDay);
        List<SittingTime> sittingTimes = sittingTimeRepository
                .findAllByRestaurantIdAndWeekDayAndStartAfterNowOrderByStart(restaurantId, weekDay);
        return sittingTimeMapper.sittingTimesToSittingTimeResponseDTOs(sittingTimes);
    }

    /**
     * {@inheritDoc}
     * <p>
     * This method removes a {@link SittingTime} by its ID.
     *
     * @param id the sitting time ID
     * @return true if the sitting time was successfully removed, false otherwise
     * @throws EntityNotFoundException if the sitting time is not found
     * @throws EntityDeletionException if there is an error during sitting time deletion
     */
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

    /**
     * Generates sitting times for a given week day information and time range.
     *
     * @param weekDayInfo the week day information
     * @param start       the start time
     * @param end         the end time
     * @param minutes     the duration of each sitting time in minutes
     * @return the list of generated sitting times
     */
    private List<SittingTime> generateSittingTimes(
            WeekDayInfo weekDayInfo,
            LocalTime start,
            LocalTime end,
            int minutes
    ) {
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

    /**
     * Saves a sitting time.
     *
     * @param sittingTime the sitting time
     * @return the saved sitting time
     * @throws EntityCreationException if there is an error during sitting time creation
     */
    private SittingTime saveSittingTime(SittingTime sittingTime) {
        try {
            return sittingTimeRepository.save(sittingTime);
        } catch (Exception e) {
            throw new EntityCreationException("sitting time");
        }
    }

    /**
     * Checks if the given week day is valid.
     *
     * @param weekDay the week day
     * @throws InvalidWeekDayException if the week day is invalid
     */
    private void checkWeekDay(int weekDay) {
        if (weekDay >= 1 && weekDay <= 7) return;

        throw new InvalidWeekDayException(weekDay);
    }

    /**
     * Removes bookings associated with a sitting time.
     *
     * @param sittingTime the sitting time
     */
    private void removeBookings(SittingTime sittingTime) {
        sittingTime.getBookings().forEach(booking ->
                // Booking owner is notified by email about the deletion. This is why I don't delete bookings in SittingTime model.
                bookingService.remove(booking.getId())
        );
    }
}