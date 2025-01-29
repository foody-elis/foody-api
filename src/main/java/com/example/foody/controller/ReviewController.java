package com.example.foody.controller;

import com.example.foody.dto.request.ReviewRequestDTO;
import com.example.foody.dto.response.ReviewResponseDTO;
import com.example.foody.exceptions.entity.EntityCreationException;
import com.example.foody.exceptions.entity.EntityDeletionException;
import com.example.foody.exceptions.entity.EntityNotFoundException;
import com.example.foody.exceptions.review.ForbiddenReviewAccessException;
import com.example.foody.exceptions.review.ReviewNotAllowedException;
import com.example.foody.model.user.User;
import com.example.foody.service.ReviewService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for handling review-related requests.
 */
@RestController
@RequestMapping("/api/v1/reviews")
@AllArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    /**
     * Saves a new review.
     *
     * @param reviewRequestDTO the review request data transfer object
     * @return the response entity containing the review response data transfer object
     * @throws EntityNotFoundException   if the entity is not found
     * @throws ReviewNotAllowedException if the review is not allowed
     * @throws EntityCreationException   if there is an error creating the entity
     */
    @PostMapping
    public ResponseEntity<ReviewResponseDTO> saveReview(@Valid @RequestBody ReviewRequestDTO reviewRequestDTO)
            throws EntityNotFoundException, ReviewNotAllowedException, EntityCreationException {
        ReviewResponseDTO reviewResponseDTO = reviewService.save(reviewRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(reviewResponseDTO);
    }

    /**
     * Retrieves all reviews.
     *
     * @return the response entity containing the list of review response data transfer objects
     */
    @GetMapping
    public ResponseEntity<List<ReviewResponseDTO>> getReviews() {
        List<ReviewResponseDTO> reviews = reviewService.findAll();
        return ResponseEntity.ok(reviews);
    }

    /**
     * Retrieves a review by its ID.
     *
     * @param id the review ID
     * @return the response entity containing the review response data transfer object
     * @throws EntityNotFoundException        if the entity is not found
     * @throws ForbiddenReviewAccessException if access to the review is forbidden
     */
    @GetMapping(path = "/{id}")
    public ResponseEntity<ReviewResponseDTO> getReviewById(@PathVariable long id)
            throws EntityNotFoundException, ForbiddenReviewAccessException {
        ReviewResponseDTO reviewResponseDTO = reviewService.findById(id);
        return ResponseEntity.ok(reviewResponseDTO);
    }

    /**
     * Retrieves all reviews for a specific customer.
     *
     * @param customer the authenticated customer
     * @return the response entity containing the list of review response data transfer objects
     * @throws EntityNotFoundException if the entity is not found
     */
    @GetMapping(path = "/customer")
    public ResponseEntity<List<ReviewResponseDTO>> getReviewsByCustomer(@AuthenticationPrincipal User customer)
            throws EntityNotFoundException {
        List<ReviewResponseDTO> reviews = reviewService.findAllByCustomer(customer.getId());
        return ResponseEntity.ok(reviews);
    }

    /**
     * Retrieves all reviews for a specific restaurant.
     *
     * @param restaurantId the restaurant ID
     * @return the response entity containing the list of review response data transfer objects
     * @throws EntityNotFoundException if the entity is not found
     */
    @GetMapping(path = "/restaurant/{restaurant-id}")
    public ResponseEntity<List<ReviewResponseDTO>> getReviewsByRestaurant(@PathVariable("restaurant-id") long restaurantId)
            throws EntityNotFoundException {
        List<ReviewResponseDTO> reviews = reviewService.findAllByRestaurant(restaurantId);
        return ResponseEntity.ok(reviews);
    }

    /**
     * Retrieves all reviews for a specific dish.
     *
     * @param dishId the dish ID
     * @return the response entity containing the list of review response data transfer objects
     * @throws EntityNotFoundException if the entity is not found
     */
    @GetMapping(path = "/dish/{dish-id}")
    public ResponseEntity<List<ReviewResponseDTO>> getReviewsByDish(@PathVariable("dish-id") long dishId)
            throws EntityNotFoundException {
        List<ReviewResponseDTO> reviews = reviewService.findAllByDish(dishId);
        return ResponseEntity.ok(reviews);
    }

    /**
     * Removes a review.
     *
     * @param id the review ID
     * @return the response entity with no content
     * @throws EntityNotFoundException        if the entity is not found
     * @throws ForbiddenReviewAccessException if access to the review is forbidden
     * @throws EntityDeletionException        if there is an error deleting the entity
     */
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> removeReview(@PathVariable long id)
            throws EntityNotFoundException, ForbiddenReviewAccessException, EntityDeletionException {
        reviewService.remove(id);
        return ResponseEntity.ok().build();
    }
}