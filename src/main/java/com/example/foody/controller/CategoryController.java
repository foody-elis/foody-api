package com.example.foody.controller;

import com.example.foody.dto.request.CategoryRequestDTO;
import com.example.foody.dto.response.CategoryResponseDTO;
import com.example.foody.dto.response.RestaurantResponseDTO;
import com.example.foody.exceptions.entity.EntityCreationException;
import com.example.foody.exceptions.entity.EntityDeletionException;
import com.example.foody.exceptions.entity.EntityDuplicateException;
import com.example.foody.service.CategoryService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<CategoryResponseDTO> saveCategory(@Valid @RequestBody CategoryRequestDTO categoryRequestDTO) throws EntityDuplicateException, EntityCreationException {
        return new ResponseEntity<>(categoryService.save(categoryRequestDTO), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponseDTO>> getCategories() {
        return new ResponseEntity<>(categoryService.findAll(), HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<CategoryResponseDTO> getCategoryById(@PathVariable long id) throws EntityNotFoundException {
        return new ResponseEntity<>(categoryService.findById(id), HttpStatus.OK);
    }

    @GetMapping(path = "/{id}/restaurants")
    public ResponseEntity<List<RestaurantResponseDTO>> getCategoryRestaurants(@PathVariable long id) throws EntityNotFoundException {
        return new ResponseEntity<>(categoryService.getRestaurants(id), HttpStatus.OK);
    }

    @DeleteMapping(path = "/delete")
    public ResponseEntity<Void> removeCategory(@RequestParam long id) throws EntityNotFoundException, EntityDeletionException {
        categoryService.remove(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
