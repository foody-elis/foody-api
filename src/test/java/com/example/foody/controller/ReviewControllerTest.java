package com.example.foody.controller;

import com.example.foody.TestDataUtil;
import com.example.foody.dto.request.ReviewRequestDTO;
import com.example.foody.dto.response.ReviewResponseDTO;
import com.example.foody.exceptions.entity.EntityNotFoundException;
import com.example.foody.model.user.User;
import com.example.foody.service.ReviewService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/**
 * Test class for the endpoints in the {@link ReviewController} class using mock services.
 */
@ExtendWith(MockitoExtension.class)
public class ReviewControllerTest {

    @InjectMocks
    private ReviewController reviewController;

    @Mock
    private ReviewService reviewService;

    @Test
    void saveReviewWhenValidRequestReturnsCreatedResponse() {
        // Arrange
        ReviewRequestDTO requestDTO = TestDataUtil.createTestReviewRequestDTO();
        ReviewResponseDTO responseDTO = TestDataUtil.createTestReviewResponseDTO();

        when(reviewService.save(requestDTO)).thenReturn(responseDTO);

        // Act
        ResponseEntity<ReviewResponseDTO> response = reviewController.saveReview(requestDTO);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(responseDTO, response.getBody());
    }

    @Test
    void getReviewsWhenCalledReturnsOkResponse() {
        // Arrange
        List<ReviewResponseDTO> responseDTOs =
                Collections.singletonList(TestDataUtil.createTestReviewResponseDTO());

        when(reviewService.findAll()).thenReturn(responseDTOs);

        // Act
        ResponseEntity<List<ReviewResponseDTO>> response = reviewController.getReviews();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDTOs, response.getBody());
    }

    @Test
    void getReviewByIdWhenValidIdReturnsOkResponse() {
        // Arrange
        ReviewResponseDTO responseDTO = TestDataUtil.createTestReviewResponseDTO();

        when(reviewService.findById(1L)).thenReturn(responseDTO);

        // Act
        ResponseEntity<ReviewResponseDTO> response = reviewController.getReviewById(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDTO, response.getBody());
    }

    @Test
    void getReviewByIdWhenInvalidIdThrowsEntityNotFoundException() {
        // Arrange
        when(reviewService.findById(1L)).thenThrow(new EntityNotFoundException("review", "id", 1L));

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> reviewController.getReviewById(1L));
    }

    @Test
    void getReviewsByCustomerWhenCalledReturnsOkResponse() {
        // Arrange
        List<ReviewResponseDTO> responseDTOs =
                Collections.singletonList(TestDataUtil.createTestReviewResponseDTO());
        User customer = TestDataUtil.createTestCustomerUser();

        when(reviewService.findAllByCustomer(customer.getId())).thenReturn(responseDTOs);

        // Act
        ResponseEntity<List<ReviewResponseDTO>> response = reviewController.getReviewsByCustomer(customer);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDTOs, response.getBody());
    }

    @Test
    void getReviewsByRestaurantWhenValidIdReturnsOkResponse() {
        // Arrange
        List<ReviewResponseDTO> responseDTOs =
                Collections.singletonList(TestDataUtil.createTestReviewResponseDTO());

        when(reviewService.findAllByRestaurant(1L)).thenReturn(responseDTOs);

        // Act
        ResponseEntity<List<ReviewResponseDTO>> response = reviewController.getReviewsByRestaurant(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDTOs, response.getBody());
    }

    @Test
    void getReviewsByDishWhenValidIdReturnsOkResponse() {
        // Arrange
        List<ReviewResponseDTO> responseDTOs =
                Collections.singletonList(TestDataUtil.createTestReviewResponseDTO());

        when(reviewService.findAllByDish(1L)).thenReturn(responseDTOs);

        // Act
        ResponseEntity<List<ReviewResponseDTO>> response = reviewController.getReviewsByDish(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDTOs, response.getBody());
    }

    @Test
    void removeReviewWhenValidIdRemovesReview() {
        // Arrange
        when(reviewService.remove(1L)).thenReturn(true);

        // Act
        ResponseEntity<Void> response = reviewController.removeReview(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(reviewService, times(1)).remove(1L);
    }
}