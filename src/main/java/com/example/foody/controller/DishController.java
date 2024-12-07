package com.example.foody.controller;

import com.example.foody.dto.request.DishRequestDTO;
import com.example.foody.dto.response.DishResponseDTO;
import com.example.foody.exceptions.entity.EntityCreationException;
import com.example.foody.exceptions.entity.EntityDeletionException;
import com.example.foody.exceptions.entity.EntityNotFoundException;
import com.example.foody.exceptions.google_drive.GoogleDriveFileUploadException;
import com.example.foody.exceptions.restaurant.ForbiddenRestaurantAccessException;
import com.example.foody.service.DishService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/dishes")
public class DishController {
    private final DishService dishService;

    public DishController(DishService dishService) {
        this.dishService = dishService;
    }

    @PostMapping
    public ResponseEntity<DishResponseDTO> saveDish(@Valid @RequestBody DishRequestDTO dishRequestDTO)
            throws EntityNotFoundException, ForbiddenRestaurantAccessException, GoogleDriveFileUploadException,  EntityCreationException {
        return new ResponseEntity<>(dishService.save(dishRequestDTO), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<DishResponseDTO>> getDishes() {
        return new ResponseEntity<>(dishService.findAll(), HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<DishResponseDTO> getDishById(@PathVariable long id)
            throws EntityNotFoundException {
        return new ResponseEntity<>(dishService.findById(id), HttpStatus.OK);
    }

    @GetMapping(path = "/restaurant/{restaurant-id}")
    public ResponseEntity<List<DishResponseDTO>> getDishesByRestaurant(@PathVariable("restaurant-id") long restaurantId) {
        return new ResponseEntity<>(dishService.findAllByRestaurant(restaurantId), HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> removeDish(@PathVariable long id)
            throws EntityNotFoundException, ForbiddenRestaurantAccessException, GoogleDriveFileUploadException, EntityDeletionException {
        dishService.remove(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
