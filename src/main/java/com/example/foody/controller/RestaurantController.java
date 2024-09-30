package com.example.foody.controller;

import com.example.foody.dto.request.RestaurantRequestDTO;
import com.example.foody.dto.response.RestaurantResponseDTO;
import com.example.foody.exceptions.entity.EntityCreationException;
import com.example.foody.exceptions.entity.EntityDeletionException;
import com.example.foody.exceptions.entity.EntityNotFoundException;
import com.example.foody.service.RestaurantService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/restaurants")
public class RestaurantController {
    private final RestaurantService restaurantService;

    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @PostMapping
    public ResponseEntity<RestaurantResponseDTO> saveRestaurant(@Valid @RequestBody RestaurantRequestDTO restaurantRequestDTO) throws EntityCreationException {
        return new ResponseEntity<>(restaurantService.save(restaurantRequestDTO), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<RestaurantResponseDTO>> getRestaurants() {
        return new ResponseEntity<>(restaurantService.findAll(), HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<RestaurantResponseDTO> getRestaurantById(@PathVariable long id) throws EntityNotFoundException {
        return new ResponseEntity<>(restaurantService.findById(id), HttpStatus.OK);
    }

    @DeleteMapping(path = "/delete")
    public ResponseEntity<Void> removeEvent(@RequestParam long id) throws EntityNotFoundException, EntityDeletionException {
        restaurantService.remove(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
