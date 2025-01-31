package com.example.foody.service;

import com.example.foody.dto.response.SittingTimeResponseDTO;
import com.example.foody.model.SittingTime;
import com.example.foody.model.WeekDayInfo;

import java.util.List;

/**
 * Service interface for managing sitting times.
 */
public interface SittingTimeService {

    /**
     * Creates sitting times for a given week day information.
     *
     * @param weekDayInfo the week day information
     * @return a list of created sitting times
     */
    List<SittingTime> createForWeekDayInfo(WeekDayInfo weekDayInfo);

    /**
     * Retrieves all sitting times.
     *
     * @return a list of all sitting time response data transfer objects
     */
    List<SittingTimeResponseDTO> findAll();

    /**
     * Finds all sitting times by restaurant ID.
     *
     * @param restaurantId the ID of the restaurant
     * @return a list of sitting time response data transfer objects for the specified restaurant
     */
    List<SittingTimeResponseDTO> findAllByRestaurant(long restaurantId);

    /**
     * Finds all sitting times by restaurant ID and week day.
     *
     * @param restaurantId the ID of the restaurant
     * @param weekDay the week day
     * @return a list of sitting time response data transfer objects for the specified restaurant and week day
     */
    List<SittingTimeResponseDTO> findAllByRestaurantAndWeekDay(long restaurantId, int weekDay);

    /**
     * Finds all sitting times by restaurant ID, week day, and start time after now.
     *
     * @param restaurantId the ID of the restaurant
     * @param weekDay the week day
     * @return a list of sitting time response data transfer objects for the specified restaurant, week day, and start time after now
     */
    List<SittingTimeResponseDTO> findAllByRestaurantAndWeekDayAndStartAfterNow(long restaurantId, int weekDay);

    /**
     * Removes a sitting time by its ID.
     *
     * @param id the ID of the sitting time to remove
     * @return true if the sitting time was successfully removed, false otherwise
     */
    boolean remove(long id);
}