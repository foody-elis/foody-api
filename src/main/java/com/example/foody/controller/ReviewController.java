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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reviews")
public class ReviewController {
    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping
    public ResponseEntity<ReviewResponseDTO> saveReview(@Valid @RequestBody ReviewRequestDTO reviewRequestDTO)
            throws EntityNotFoundException, ReviewNotAllowedException, EntityCreationException {
        return new ResponseEntity<>(reviewService.save(reviewRequestDTO), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ReviewResponseDTO>> getReviews() {
        return new ResponseEntity<>(reviewService.findAll(), HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<ReviewResponseDTO> getReviewById(@PathVariable long id)
            throws EntityNotFoundException, ForbiddenReviewAccessException {
        return new ResponseEntity<>(reviewService.findById(id), HttpStatus.OK);
    }

    @GetMapping(path = "/customer")
    public ResponseEntity<List<ReviewResponseDTO>> getReviewsByCustomer(@AuthenticationPrincipal User customer)
            throws EntityNotFoundException {
        return new ResponseEntity<>(reviewService.findAllByCustomer(customer.getId()), HttpStatus.OK);
    }

    @GetMapping(path = "/restaurant/{restaurant-id}")
    public ResponseEntity<List<ReviewResponseDTO>> getReviewsByRestaurant(@PathVariable("restaurant-id") long restaurantId)
            throws EntityNotFoundException {
        return new ResponseEntity<>(reviewService.findAllByRestaurant(restaurantId), HttpStatus.OK);
    }

    @GetMapping(path = "/dish/{dish-id}")
    public ResponseEntity<List<ReviewResponseDTO>> getReviewsByDish(@PathVariable("dish-id") long dishId)
            throws EntityNotFoundException {
        return new ResponseEntity<>(reviewService.findAllByDish(dishId), HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> removeReview(@PathVariable long id)
            throws EntityNotFoundException, ForbiddenReviewAccessException, EntityDeletionException {
        reviewService.remove(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}