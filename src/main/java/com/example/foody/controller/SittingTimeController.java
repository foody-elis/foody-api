package com.example.foody.controller;

import com.example.foody.dto.response.SittingTimeResponseDTO;
import com.example.foody.exceptions.entity.EntityNotFoundException;
import com.example.foody.exceptions.sitting_time.InvalidWeekDayException;
import com.example.foody.service.SittingTimeService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller for handling sitting time-related requests.
 */
@RestController
@RequestMapping("/api/v1/sitting-times")
@AllArgsConstructor
public class SittingTimeController {

    private final SittingTimeService sittingTimeService;

    /**
     * Retrieves all sitting times.
     *
     * @return the response entity containing the list of sitting time response data transfer objects
     */
    @GetMapping
    public ResponseEntity<List<SittingTimeResponseDTO>> getSittingTimes() {
        List<SittingTimeResponseDTO> sittingTimes = sittingTimeService.findAll();
        return ResponseEntity.ok(sittingTimes);
    }

    /**
     * Retrieves all sitting times for a specific restaurant.
     *
     * @param restaurantId the restaurant ID
     * @return the response entity containing the list of sitting time response data transfer objects
     * @throws EntityNotFoundException if the entity is not found
     */
    @GetMapping(path = "/restaurant/{restaurant-id}")
    public ResponseEntity<List<SittingTimeResponseDTO>> getSittingTimesByRestaurant(
            @PathVariable("restaurant-id") long restaurantId
    ) throws EntityNotFoundException {
        List<SittingTimeResponseDTO> sittingTimes = sittingTimeService.findAllByRestaurant(restaurantId);
        return ResponseEntity.ok(sittingTimes);
    }

    /**
     * Retrieves all sitting times for a specific restaurant and week day.
     *
     * @param restaurantId the restaurant ID
     * @param weekDay      the week day
     * @return the response entity containing the list of sitting time response data transfer objects
     * @throws EntityNotFoundException if the entity is not found
     * @throws InvalidWeekDayException if the week day is invalid
     */
    @GetMapping(path = "/restaurant/{restaurant-id}/week-day/{week-day}")
    public ResponseEntity<List<SittingTimeResponseDTO>> getSittingTimesByRestaurantAndWeekDay(
            @PathVariable("restaurant-id") long restaurantId,
            @PathVariable("week-day") int weekDay
    ) throws EntityNotFoundException, InvalidWeekDayException {
        List<SittingTimeResponseDTO> sittingTimes = sittingTimeService
                .findAllByRestaurantAndWeekDay(restaurantId, weekDay);
        return ResponseEntity.ok(sittingTimes);
    }

    /**
     * Retrieves all sitting times for a specific restaurant and week day that start after the current time.
     *
     * @param restaurantId the restaurant ID
     * @param weekDay      the week day
     * @return the response entity containing the list of sitting time response data transfer objects
     * @throws EntityNotFoundException if the entity is not found
     * @throws InvalidWeekDayException if the week day is invalid
     */
    @GetMapping(path = "/restaurant/{restaurant-id}/week-day/{week-day}/start-after-now")
    public ResponseEntity<List<SittingTimeResponseDTO>> getSittingTimesByRestaurantAndWeekDayAndStartAfterNow(
            @PathVariable("restaurant-id") long restaurantId,
            @PathVariable("week-day") int weekDay
    ) throws EntityNotFoundException, InvalidWeekDayException {
        List<SittingTimeResponseDTO> sittingTimes = sittingTimeService
                .findAllByRestaurantAndWeekDayAndStartAfterNow(restaurantId, weekDay);
        return ResponseEntity.ok(sittingTimes);
    }
}