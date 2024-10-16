package com.example.foody.controller;

import com.example.foody.dto.response.SittingTimeResponseDTO;
import com.example.foody.exceptions.entity.EntityNotFoundException;
import com.example.foody.exceptions.sitting_time.InvalidWeekDayException;
import com.example.foody.service.SittingTimeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/sitting-times")
public class SittingTimeController {
    private final SittingTimeService sittingTimeService;

    public SittingTimeController(SittingTimeService sittingTimeService) {
        this.sittingTimeService = sittingTimeService;
    }

    @GetMapping
    public ResponseEntity<List<SittingTimeResponseDTO>> getSittingTimes() {
        return new ResponseEntity<>(sittingTimeService.findAll(), HttpStatus.OK);
    }

    @GetMapping(path = "/restaurant/{restaurant-id}/week-day/{week-day}")
    public ResponseEntity<List<SittingTimeResponseDTO>> getSittingTimesByRestaurantAndWeekDay(@PathVariable("restaurant-id") long restaurantId, @PathVariable("week-day") int weekDay)
            throws EntityNotFoundException, InvalidWeekDayException {
        return new ResponseEntity<>(
                sittingTimeService.findAllByRestaurantAndWeekDay(restaurantId, weekDay),
                HttpStatus.OK
        );
    }

    @GetMapping(path = "/restaurant/{restaurant-id}/week-day/{week-day}/start-after-now")
    public ResponseEntity<List<SittingTimeResponseDTO>> getSittingTimesByRestaurantAndWeekDayAndStartAfterNow(@PathVariable("restaurant-id") long restaurantId, @PathVariable("week-day") int weekDay)
            throws EntityNotFoundException, InvalidWeekDayException {
        return new ResponseEntity<>(
                sittingTimeService.findAllByRestaurantAndWeekDayAndStartAfterNow(restaurantId, weekDay),
                HttpStatus.OK
        );
    }
}
