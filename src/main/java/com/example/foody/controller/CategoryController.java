package com.example.foody.controller;

import com.example.foody.dto.request.CategoryRequestDTO;
import com.example.foody.dto.response.CategoryResponseDTO;
import com.example.foody.exceptions.entity.EntityCreationException;
import com.example.foody.exceptions.entity.EntityDeletionException;
import com.example.foody.exceptions.entity.EntityDuplicateException;
import com.example.foody.service.CategoryService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for handling category-related requests.
 */
@RestController
@RequestMapping("/api/v1/categories")
@AllArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    /**
     * Saves a new category.
     *
     * @param categoryRequestDTO the category request data transfer object
     * @return the response entity containing the category response data transfer object
     * @throws EntityDuplicateException if the category already exists
     * @throws EntityCreationException  if there is an error creating the category
     */
    @PostMapping
    public ResponseEntity<CategoryResponseDTO> saveCategory(@Valid @RequestBody CategoryRequestDTO categoryRequestDTO)
            throws EntityDuplicateException, EntityCreationException {
        CategoryResponseDTO responseDTO = categoryService.save(categoryRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    /**
     * Retrieves all categories.
     *
     * @return the response entity containing the list of category response data transfer objects
     */
    @GetMapping
    public ResponseEntity<List<CategoryResponseDTO>> getCategories() {
        List<CategoryResponseDTO> responseDTOs = categoryService.findAll();
        return ResponseEntity.ok(responseDTOs);
    }

    /**
     * Retrieves a category by its ID.
     *
     * @param id the category ID
     * @return the response entity containing the category response data transfer object
     * @throws EntityNotFoundException if the category is not found
     */
    @GetMapping(path = "/{id}")
    public ResponseEntity<CategoryResponseDTO> getCategoryById(@PathVariable long id)
            throws EntityNotFoundException {
        CategoryResponseDTO responseDTO = categoryService.findById(id);
        return ResponseEntity.ok(responseDTO);
    }

    /**
     * Removes a category by its ID.
     *
     * @param id the category ID
     * @return the response entity with no content
     * @throws EntityNotFoundException if the category is not found
     * @throws EntityDeletionException if there is an error deleting the category
     */
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> removeCategory(@PathVariable long id)
            throws EntityNotFoundException, EntityDeletionException {
        categoryService.remove(id);
        return ResponseEntity.ok().build();
    }
}