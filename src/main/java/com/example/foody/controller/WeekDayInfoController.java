package com.example.foody.controller;

import com.example.foody.dto.request.WeekDayInfoRequestDTO;
import com.example.foody.dto.response.WeekDayInfoResponseDTO;
import com.example.foody.exceptions.entity.EntityCreationException;
import com.example.foody.exceptions.entity.EntityNotFoundException;
import com.example.foody.exceptions.restaurant.ForbiddenRestaurantAccessException;
import com.example.foody.service.WeekDayInfoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/week-day-infos")
public class WeekDayInfoController {
    private final WeekDayInfoService weekDayInfoService;

    public WeekDayInfoController(WeekDayInfoService weekDayInfoService) {
        this.weekDayInfoService = weekDayInfoService;
    }

    @PostMapping
    public ResponseEntity<WeekDayInfoResponseDTO> saveWeekDayInfo(@Valid @RequestBody WeekDayInfoRequestDTO weekDayInfoRequestDTO)
            throws EntityNotFoundException, ForbiddenRestaurantAccessException, EntityCreationException {
        return new ResponseEntity<>(weekDayInfoService.save(weekDayInfoRequestDTO), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<WeekDayInfoResponseDTO>> getWeekDayInfos() {
        return new ResponseEntity<>(weekDayInfoService.findAll(), HttpStatus.OK);
    }

    @GetMapping(path = "/restaurant/{restaurant-id}")
    public ResponseEntity<List<WeekDayInfoResponseDTO>> getWeekDayInfosByRestaurant(@PathVariable("restaurant-id") long restaurantId)
            throws EntityNotFoundException, ForbiddenRestaurantAccessException {
        return new ResponseEntity<>(weekDayInfoService.findAllByRestaurant(restaurantId), HttpStatus.OK);
    }
}
