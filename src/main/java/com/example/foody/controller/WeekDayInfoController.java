package com.example.foody.controller;

import com.example.foody.dto.request.WeekDayInfoRequestDTO;
import com.example.foody.dto.request.WeekDayInfoUpdateRequestDTO;
import com.example.foody.dto.response.WeekDayInfoResponseDTO;
import com.example.foody.exceptions.entity.EntityCreationException;
import com.example.foody.exceptions.entity.EntityNotFoundException;
import com.example.foody.exceptions.restaurant.ForbiddenRestaurantAccessException;
import com.example.foody.service.WeekDayInfoService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for handling week day info-related requests.
 */
@RestController
@RequestMapping("/api/v1/week-day-infos")
@AllArgsConstructor
public class WeekDayInfoController {

    private final WeekDayInfoService weekDayInfoService;

    /**
     * Saves a new week day info.
     *
     * @param weekDayInfoRequestDTO the week day info request data transfer object
     * @return the response entity containing the week day info response data transfer object
     * @throws EntityNotFoundException            if the entity is not found
     * @throws ForbiddenRestaurantAccessException if access to the restaurant is forbidden
     * @throws EntityCreationException            if there is an error creating the entity
     */
    @PostMapping
    public ResponseEntity<WeekDayInfoResponseDTO> saveWeekDayInfo(
            @Valid @RequestBody WeekDayInfoRequestDTO weekDayInfoRequestDTO
    ) throws EntityNotFoundException, ForbiddenRestaurantAccessException, EntityCreationException {
        WeekDayInfoResponseDTO responseDTO = weekDayInfoService.save(weekDayInfoRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    /**
     * Retrieves all week day infos.
     *
     * @return the response entity containing the list of week day info response data transfer objects
     */
    @GetMapping
    public ResponseEntity<List<WeekDayInfoResponseDTO>> getWeekDayInfos() {
        List<WeekDayInfoResponseDTO> responseDTOs = weekDayInfoService.findAll();
        return ResponseEntity.ok(responseDTOs);
    }

    /**
     * Retrieves all week day infos for a specific restaurant.
     *
     * @param restaurantId the restaurant ID
     * @return the response entity containing the list of week day info response data transfer objects
     * @throws EntityNotFoundException            if the entity is not found
     * @throws ForbiddenRestaurantAccessException if access to the restaurant is forbidden
     */
    @GetMapping(path = "/restaurant/{restaurant-id}")
    public ResponseEntity<List<WeekDayInfoResponseDTO>> getWeekDayInfosByRestaurant(
            @PathVariable("restaurant-id") long restaurantId
    ) throws EntityNotFoundException, ForbiddenRestaurantAccessException {
        List<WeekDayInfoResponseDTO> responseDTOs = weekDayInfoService.findAllByRestaurant(restaurantId);
        return ResponseEntity.ok(responseDTOs);
    }

    /**
     * Updates a week day info.
     *
     * @param id                          the week day info ID
     * @param weekDayInfoUpdateRequestDTO the week day info update request data transfer object
     * @return the response entity containing the updated week day info response data transfer object
     * @throws EntityNotFoundException            if the entity is not found
     * @throws ForbiddenRestaurantAccessException if access to the restaurant is forbidden
     */
    @PutMapping(path = "/{id}")
    public ResponseEntity<WeekDayInfoResponseDTO> updateWeekDayInfo(
            @PathVariable("id") long id,
            @Valid @RequestBody WeekDayInfoUpdateRequestDTO weekDayInfoUpdateRequestDTO
    ) throws EntityNotFoundException, ForbiddenRestaurantAccessException {
        WeekDayInfoResponseDTO responseDTO = weekDayInfoService.update(id, weekDayInfoUpdateRequestDTO);
        return ResponseEntity.ok(responseDTO);
    }
}