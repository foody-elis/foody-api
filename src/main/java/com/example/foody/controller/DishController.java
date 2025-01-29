package com.example.foody.controller;

import com.example.foody.dto.request.DishRequestDTO;
import com.example.foody.dto.request.DishUpdateRequestDTO;
import com.example.foody.dto.response.DishResponseDTO;
import com.example.foody.exceptions.entity.EntityCreationException;
import com.example.foody.exceptions.entity.EntityDeletionException;
import com.example.foody.exceptions.entity.EntityEditException;
import com.example.foody.exceptions.entity.EntityNotFoundException;
import com.example.foody.exceptions.google_drive.GoogleDriveFileDeleteException;
import com.example.foody.exceptions.google_drive.GoogleDriveFileUploadException;
import com.example.foody.exceptions.restaurant.ForbiddenRestaurantAccessException;
import com.example.foody.service.DishService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for handling dish-related requests.
 */
@RestController
@RequestMapping("/api/v1/dishes")
@AllArgsConstructor
public class DishController {

    private final DishService dishService;

    /**
     * Saves a new dish.
     *
     * @param dishRequestDTO the dish request data transfer object
     * @return the response entity containing the dish response data transfer object
     * @throws EntityNotFoundException            if the entity is not found
     * @throws ForbiddenRestaurantAccessException if access to the restaurant is forbidden
     * @throws GoogleDriveFileUploadException     if there is an error uploading a file to Google Drive
     * @throws EntityCreationException            if there is an error creating the entity
     */
    @PostMapping
    public ResponseEntity<DishResponseDTO> saveDish(
            @Valid @RequestBody DishRequestDTO dishRequestDTO
    ) throws EntityNotFoundException, ForbiddenRestaurantAccessException, GoogleDriveFileUploadException, EntityCreationException {
        DishResponseDTO dishResponseDTO = dishService.save(dishRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(dishResponseDTO);
    }

    /**
     * Retrieves all dishes.
     *
     * @return the response entity containing the list of dish response data transfer objects
     */
    @GetMapping
    public ResponseEntity<List<DishResponseDTO>> getDishes() {
        List<DishResponseDTO> dishes = dishService.findAll();
        return ResponseEntity.ok(dishes);
    }

    /**
     * Retrieves a dish by its ID.
     *
     * @param id the dish ID
     * @return the response entity containing the dish response data transfer object
     * @throws EntityNotFoundException if the entity is not found
     */
    @GetMapping(path = "/{id}")
    public ResponseEntity<DishResponseDTO> getDishById(@PathVariable long id)
            throws EntityNotFoundException {
        DishResponseDTO dishResponseDTO = dishService.findById(id);
        return ResponseEntity.ok(dishResponseDTO);
    }

    /**
     * Retrieves all dishes for a specific restaurant.
     *
     * @param restaurantId the restaurant ID
     * @return the response entity containing the list of dish response data transfer objects
     */
    @GetMapping(path = "/restaurant/{restaurant-id}")
    public ResponseEntity<List<DishResponseDTO>> getDishesByRestaurant(@PathVariable("restaurant-id") long restaurantId) {
        List<DishResponseDTO> dishes = dishService.findAllByRestaurant(restaurantId);
        return ResponseEntity.ok(dishes);
    }

    /**
     * Updates a dish.
     *
     * @param id                   the dish ID
     * @param dishUpdateRequestDTO the dish update request data transfer object
     * @return the response entity containing the updated dish response data transfer object
     * @throws EntityNotFoundException            if the entity is not found
     * @throws ForbiddenRestaurantAccessException if access to the restaurant is forbidden
     * @throws GoogleDriveFileUploadException     if there is an error uploading a file to Google Drive
     * @throws GoogleDriveFileDeleteException     if there is an error deleting a file from Google Drive
     * @throws EntityEditException                if there is an error editing the entity
     */
    @PutMapping(path = "/{id}")
    public ResponseEntity<DishResponseDTO> updateDish(
            @PathVariable long id,
            @Valid @RequestBody DishUpdateRequestDTO dishUpdateRequestDTO
    ) throws EntityNotFoundException, ForbiddenRestaurantAccessException, GoogleDriveFileUploadException,
            GoogleDriveFileDeleteException, EntityEditException {
        DishResponseDTO dishResponseDTO = dishService.update(id, dishUpdateRequestDTO);
        return ResponseEntity.ok(dishResponseDTO);
    }

    /**
     * Removes a dish.
     *
     * @param id the dish ID
     * @return the response entity with no content
     * @throws EntityNotFoundException            if the entity is not found
     * @throws ForbiddenRestaurantAccessException if access to the restaurant is forbidden
     * @throws GoogleDriveFileDeleteException     if there is an error deleting a file from Google Drive
     * @throws EntityDeletionException            if there is an error deleting the entity
     */
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> removeDish(
            @PathVariable long id
    ) throws EntityNotFoundException, ForbiddenRestaurantAccessException, GoogleDriveFileDeleteException, EntityDeletionException {
        dishService.remove(id);
        return ResponseEntity.ok().build();
    }
}