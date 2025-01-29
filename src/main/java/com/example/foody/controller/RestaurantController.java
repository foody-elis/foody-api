package com.example.foody.controller;

import com.example.foody.dto.request.RestaurantRequestDTO;
import com.example.foody.dto.response.DetailedRestaurantResponseDTO;
import com.example.foody.dto.response.RestaurantResponseDTO;
import com.example.foody.exceptions.entity.EntityCreationException;
import com.example.foody.exceptions.entity.EntityDeletionException;
import com.example.foody.exceptions.entity.EntityEditException;
import com.example.foody.exceptions.entity.EntityNotFoundException;
import com.example.foody.exceptions.google_drive.GoogleDriveFileDeleteException;
import com.example.foody.exceptions.google_drive.GoogleDriveFileUploadException;
import com.example.foody.exceptions.restaurant.ForbiddenRestaurantAccessException;
import com.example.foody.exceptions.restaurant.RestaurateurAlreadyHasRestaurantException;
import com.example.foody.model.user.RestaurateurUser;
import com.example.foody.service.RestaurantService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for handling restaurant-related requests.
 */
@RestController
@RequestMapping("/api/v1/restaurants")
@AllArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;

    /**
     * Saves a new restaurant.
     *
     * @param restaurantRequestDTO the restaurant request data transfer object
     * @return the response entity containing the restaurant response data transfer object
     * @throws RestaurateurAlreadyHasRestaurantException if the restaurateur already has a restaurant
     * @throws GoogleDriveFileUploadException            if there is an error uploading a file to Google Drive
     * @throws EntityCreationException                   if there is an error creating the entity
     */
    @PostMapping
    public ResponseEntity<RestaurantResponseDTO> saveRestaurant(
            @Valid @RequestBody RestaurantRequestDTO restaurantRequestDTO
    ) throws RestaurateurAlreadyHasRestaurantException, GoogleDriveFileUploadException, EntityCreationException {
        RestaurantResponseDTO restaurantResponseDTO = restaurantService.save(restaurantRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(restaurantResponseDTO);
    }

    /**
     * Retrieves all restaurants.
     *
     * @return the response entity containing the list of detailed restaurant response data transfer objects
     */
    @GetMapping
    public ResponseEntity<List<DetailedRestaurantResponseDTO>> getRestaurants() {
        List<DetailedRestaurantResponseDTO> restaurants = restaurantService.findAll();
        return ResponseEntity.ok(restaurants);
    }

    /**
     * Retrieves a restaurant by its ID.
     *
     * @param id the restaurant ID
     * @return the response entity containing the detailed restaurant response data transfer object
     * @throws EntityNotFoundException if the entity is not found
     */
    @GetMapping(path = "/{id}")
    public ResponseEntity<DetailedRestaurantResponseDTO> getRestaurantById(@PathVariable long id)
            throws EntityNotFoundException {
        DetailedRestaurantResponseDTO restaurantResponseDTO = restaurantService.findById(id);
        return ResponseEntity.ok(restaurantResponseDTO);
    }

    /**
     * Retrieves a restaurant by the authenticated restaurateur.
     *
     * @param restaurateur the authenticated restaurateur
     * @return the response entity containing the detailed restaurant response data transfer object
     * @throws EntityNotFoundException if the entity is not found
     */
    @GetMapping(path = "/restaurateur")
    public ResponseEntity<DetailedRestaurantResponseDTO> getRestaurantByRestaurateur(
            @AuthenticationPrincipal RestaurateurUser restaurateur
    ) throws EntityNotFoundException {
        DetailedRestaurantResponseDTO restaurantResponseDTO = restaurantService.findByRestaurateur(restaurateur.getId());
        return ResponseEntity.ok(restaurantResponseDTO);
    }

    /**
     * Retrieves all restaurants by category.
     *
     * @param categoryId the category ID
     * @return the response entity containing the list of detailed restaurant response data transfer objects
     * @throws EntityNotFoundException if the entity is not found
     */
    @GetMapping(path = "/category/{category-id}")
    public ResponseEntity<List<DetailedRestaurantResponseDTO>> getRestaurantsByCategory(
            @PathVariable("category-id") long categoryId
    ) throws EntityNotFoundException {
        List<DetailedRestaurantResponseDTO> restaurants = restaurantService.findAllByCategory(categoryId);
        return ResponseEntity.ok(restaurants);
    }

    /**
     * Approves a restaurant.
     *
     * @param id the restaurant ID
     * @return the response entity containing the updated detailed restaurant response data transfer object
     * @throws EntityNotFoundException if the entity is not found
     * @throws EntityEditException     if there is an error editing the entity
     */
    @PatchMapping(path = "/approve/{id}")
    public ResponseEntity<DetailedRestaurantResponseDTO> approveRestaurant(@PathVariable long id)
            throws EntityNotFoundException, EntityEditException {
        DetailedRestaurantResponseDTO restaurantResponseDTO = restaurantService.approveById(id);
        return ResponseEntity.ok(restaurantResponseDTO);
    }

    /**
     * Updates a restaurant.
     *
     * @param id                   the restaurant ID
     * @param restaurantRequestDTO the restaurant request data transfer object
     * @return the response entity containing the updated detailed restaurant response data transfer object
     * @throws EntityNotFoundException            if the entity is not found
     * @throws ForbiddenRestaurantAccessException if access to the restaurant is forbidden
     * @throws GoogleDriveFileUploadException     if there is an error uploading a file to Google Drive
     * @throws GoogleDriveFileDeleteException     if there is an error deleting a file from Google Drive
     * @throws EntityEditException                if there is an error editing the entity
     */
    @PutMapping(path = "/{id}")
    public ResponseEntity<DetailedRestaurantResponseDTO> updateRestaurant(
            @PathVariable long id,
            @Valid @RequestBody RestaurantRequestDTO restaurantRequestDTO
    ) throws EntityNotFoundException, ForbiddenRestaurantAccessException, GoogleDriveFileUploadException,
            GoogleDriveFileDeleteException, EntityEditException {
        DetailedRestaurantResponseDTO restaurantResponseDTO = restaurantService.update(id, restaurantRequestDTO);
        return ResponseEntity.ok(restaurantResponseDTO);
    }

    /**
     * Removes a restaurant.
     *
     * @param id the restaurant ID
     * @return the response entity with no content
     * @throws EntityNotFoundException        if the entity is not found
     * @throws GoogleDriveFileDeleteException if there is an error deleting a file from Google Drive
     * @throws EntityDeletionException        if there is an error deleting the entity
     */
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> removeRestaurant(@PathVariable long id)
            throws EntityNotFoundException, GoogleDriveFileDeleteException, EntityDeletionException {
        restaurantService.remove(id);
        return ResponseEntity.ok().build();
    }
}