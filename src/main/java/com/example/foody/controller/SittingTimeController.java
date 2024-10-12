package com.example.foody.controller;

import com.example.foody.dto.request.SittingTimeRequestDTO;
import com.example.foody.dto.response.SittingTimeResponseDTO;
import com.example.foody.exceptions.entity.EntityCreationException;
import com.example.foody.exceptions.entity.EntityNotFoundException;
import com.example.foody.exceptions.sitting_time.InvalidWeekDayException;
import com.example.foody.service.SittingTimeService;
import jakarta.validation.Valid;
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

    @PostMapping
    public ResponseEntity<SittingTimeResponseDTO> saveSittingTime(@Valid @RequestBody SittingTimeRequestDTO sittingTimeRequestDTO)
            throws EntityNotFoundException, EntityCreationException {
        return new ResponseEntity<>(sittingTimeService.save(sittingTimeRequestDTO), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<SittingTimeResponseDTO>> getSittingTimes() {
        return new ResponseEntity<>(sittingTimeService.findAll(), HttpStatus.OK);
    }

    @GetMapping(path = "/restaurant/{restaurant-id}")
    public ResponseEntity<List<SittingTimeResponseDTO>> getSittingTimesByRestaurant(@PathVariable("restaurant-id") long restaurantId)
            throws EntityNotFoundException {
        return new ResponseEntity<>(sittingTimeService.findAllByRestaurant(restaurantId), HttpStatus.OK);
    }

    @GetMapping(path = "/restaurant/{restaurant-id}/week-day/{week-day}")
    public ResponseEntity<List<SittingTimeResponseDTO>> getSittingTimesByRestaurantAndWeekDay(@PathVariable("restaurant-id") long restaurantId, @PathVariable("week-day") int weekDay)
            throws EntityNotFoundException, InvalidWeekDayException {
        return new ResponseEntity<>(
                sittingTimeService.findAllByRestaurantAndWeekDayAndStartTimeAfterNow(restaurantId, weekDay),
                HttpStatus.OK
        );
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> removeSittingTime(@PathVariable long id)
            throws EntityNotFoundException {
        sittingTimeService.remove(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
