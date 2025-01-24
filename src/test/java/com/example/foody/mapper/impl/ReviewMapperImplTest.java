package com.example.foody.mapper.impl;

import com.example.foody.builder.ReviewBuilder;
import com.example.foody.dto.request.ReviewRequestDTO;
import com.example.foody.dto.response.ReviewResponseDTO;
import com.example.foody.model.Dish;
import com.example.foody.model.Restaurant;
import com.example.foody.model.Review;
import com.example.foody.model.user.CustomerUser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReviewMapperImplTest {

    @InjectMocks
    private ReviewMapperImpl reviewMapper;

    @Mock
    private ReviewBuilder reviewBuilder;

    @Test
    void reviewToReviewResponseDTOWhenReviewIsNullReturnsNull() {
        // Act
        ReviewResponseDTO result = reviewMapper.reviewToReviewResponseDTO(null);

        // Assert
        assertNull(result);
    }

    @Test
    void reviewToReviewResponseDTOWhenValidReturnsDTO() {
        // Arrange
        Review review = mock(Review.class);
        Restaurant restaurant = mock(Restaurant.class);
        Dish dish = mock(Dish.class);
        CustomerUser customer = mock(CustomerUser.class);

        when(review.getId()).thenReturn(1L);
        when(review.getCreatedAt()).thenReturn(LocalDateTime.of(2025, 1, 1, 0, 0));
        when(review.getTitle()).thenReturn("Test Title");
        when(review.getDescription()).thenReturn("Test Description");
        when(review.getRating()).thenReturn(4);
        when(review.getRestaurant()).thenReturn(restaurant);
        when(restaurant.getId()).thenReturn(10L);
        when(review.getDish()).thenReturn(dish);
        when(dish.getId()).thenReturn(20L);
        when(review.getCustomer()).thenReturn(customer);
        when(customer.getId()).thenReturn(30L);
        when(customer.getName()).thenReturn("John");
        when(customer.getSurname()).thenReturn("Doe");
        when(customer.getAvatarUrl()).thenReturn("avatar.jpg");

        // Act
        ReviewResponseDTO result = reviewMapper.reviewToReviewResponseDTO(review);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(LocalDateTime.of(2025, 1, 1, 0, 0), result.getCreatedAt());
        assertEquals("Test Title", result.getTitle());
        assertEquals("Test Description", result.getDescription());
        assertEquals(4, result.getRating());
        assertEquals(10L, result.getRestaurantId());
        assertEquals(20L, result.getDishId());
        assertEquals(30L, result.getCustomerId());
        assertEquals("John", result.getCustomerName());
        assertEquals("Doe", result.getCustomerSurname());
        assertEquals("avatar.jpg", result.getCustomerAvatarUrl());
    }

    @Test
    void reviewToReviewResponseDTOWhenRestaurantIsNullReturnsDTO() {
        // Arrange
        Review review = mock(Review.class);
        Dish dish = mock(Dish.class);
        CustomerUser customer = mock(CustomerUser.class);

        when(review.getId()).thenReturn(1L);
        when(review.getCreatedAt()).thenReturn(LocalDateTime.of(2025, 1, 1, 0, 0));
        when(review.getTitle()).thenReturn("Test Title");
        when(review.getDescription()).thenReturn("Test Description");
        when(review.getRating()).thenReturn(4);
        when(review.getDish()).thenReturn(dish);
        when(dish.getId()).thenReturn(20L);
        when(review.getCustomer()).thenReturn(customer);
        when(customer.getId()).thenReturn(30L);
        when(customer.getName()).thenReturn("John");
        when(customer.getSurname()).thenReturn("Doe");
        when(customer.getAvatarUrl()).thenReturn("avatar.jpg");

        // Act
        ReviewResponseDTO result = reviewMapper.reviewToReviewResponseDTO(review);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(LocalDateTime.of(2025, 1, 1, 0, 0), result.getCreatedAt());
        assertEquals("Test Title", result.getTitle());
        assertEquals("Test Description", result.getDescription());
        assertEquals(4, result.getRating());
        assertNull(result.getRestaurantId());
        assertEquals(20L, result.getDishId());
        assertEquals(30L, result.getCustomerId());
        assertEquals("John", result.getCustomerName());
        assertEquals("Doe", result.getCustomerSurname());
        assertEquals("avatar.jpg", result.getCustomerAvatarUrl());
    }

    @Test
    void reviewToReviewResponseDTOWhenDishIsNullReturnsDTO() {
        // Arrange
        Review review = mock(Review.class);
        Restaurant restaurant = mock(Restaurant.class);
        CustomerUser customer = mock(CustomerUser.class);

        when(review.getId()).thenReturn(1L);
        when(review.getCreatedAt()).thenReturn(LocalDateTime.of(2025, 1, 1, 0, 0));
        when(review.getTitle()).thenReturn("Test Title");
        when(review.getDescription()).thenReturn("Test Description");
        when(review.getRating()).thenReturn(4);
        when(review.getRestaurant()).thenReturn(restaurant);
        when(restaurant.getId()).thenReturn(10L);
        when(review.getCustomer()).thenReturn(customer);
        when(customer.getId()).thenReturn(30L);
        when(customer.getName()).thenReturn("John");
        when(customer.getSurname()).thenReturn("Doe");
        when(customer.getAvatarUrl()).thenReturn("avatar.jpg");

        // Act
        ReviewResponseDTO result = reviewMapper.reviewToReviewResponseDTO(review);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(LocalDateTime.of(2025, 1, 1, 0, 0), result.getCreatedAt());
        assertEquals("Test Title", result.getTitle());
        assertEquals("Test Description", result.getDescription());
        assertEquals(4, result.getRating());
        assertEquals(10L, result.getRestaurantId());
        assertNull(result.getDishId());
        assertEquals(30L, result.getCustomerId());
        assertEquals("John", result.getCustomerName());
        assertEquals("Doe", result.getCustomerSurname());
        assertEquals("avatar.jpg", result.getCustomerAvatarUrl());
    }

    @Test
    void reviewToReviewResponseDTOWhenCustomerIsNullReturnsDTO() {
        // Arrange
        Review review = mock(Review.class);
        Restaurant restaurant = mock(Restaurant.class);
        Dish dish = mock(Dish.class);

        when(review.getId()).thenReturn(1L);
        when(review.getCreatedAt()).thenReturn(LocalDateTime.of(2025, 1, 1, 0, 0));
        when(review.getTitle()).thenReturn("Test Title");
        when(review.getDescription()).thenReturn("Test Description");
        when(review.getRating()).thenReturn(4);
        when(review.getRestaurant()).thenReturn(restaurant);
        when(restaurant.getId()).thenReturn(10L);
        when(review.getDish()).thenReturn(dish);
        when(dish.getId()).thenReturn(20L);

        // Act
        ReviewResponseDTO result = reviewMapper.reviewToReviewResponseDTO(review);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(LocalDateTime.of(2025, 1, 1, 0, 0), result.getCreatedAt());
        assertEquals("Test Title", result.getTitle());
        assertEquals("Test Description", result.getDescription());
        assertEquals(4, result.getRating());
        assertEquals(10L, result.getRestaurantId());
        assertEquals(20L, result.getDishId());
        assertNull(result.getCustomerId());
        assertNull(result.getCustomerName());
        assertNull(result.getCustomerSurname());
        assertNull(result.getCustomerAvatarUrl());
    }

    @Test
    void reviewRequestDTOToReviewWhenRequestIsNullReturnsNull() {
        // Act
        Review result = reviewMapper.reviewRequestDTOToReview(null);

        // Assert
        assertNull(result);
    }

    @Test
    void reviewRequestDTOToReviewWhenValidReturnsReview() {
        // Arrange
        ReviewRequestDTO requestDTO = mock(ReviewRequestDTO.class);
        when(requestDTO.getTitle()).thenReturn("Test Title");
        when(requestDTO.getDescription()).thenReturn("Test Description");
        when(requestDTO.getRating()).thenReturn(4);

        Review review = mock(Review.class);
        when(reviewBuilder.title("Test Title")).thenReturn(reviewBuilder);
        when(reviewBuilder.description("Test Description")).thenReturn(reviewBuilder);
        when(reviewBuilder.rating(4)).thenReturn(reviewBuilder);
        when(reviewBuilder.build()).thenReturn(review);

        // Act
        Review result = reviewMapper.reviewRequestDTOToReview(requestDTO);

        // Assert
        assertNotNull(result);
        verify(reviewBuilder).title("Test Title");
        verify(reviewBuilder).description("Test Description");
        verify(reviewBuilder).rating(4);
        verify(reviewBuilder).build();
    }

    @Test
    void reviewsToReviewResponseDTOsWhenReviewsIsNullReturnsNull() {
        // Act
        List<ReviewResponseDTO> result = reviewMapper.reviewsToReviewResponseDTOs(null);

        // Assert
        assertNull(result);
    }

    @Test
    void reviewsToReviewResponseDTOsWhenValidReturnsDTOList() {
        // Arrange
        Review review = mock(Review.class);
        Restaurant restaurant = mock(Restaurant.class);
        Dish dish = mock(Dish.class);
        CustomerUser customer = mock(CustomerUser.class);

        when(review.getId()).thenReturn(1L);
        when(review.getCreatedAt()).thenReturn(LocalDateTime.of(2025, 1, 1, 0, 0));
        when(review.getTitle()).thenReturn("Test Title");
        when(review.getDescription()).thenReturn("Test Description");
        when(review.getRating()).thenReturn(4);
        when(review.getRestaurant()).thenReturn(restaurant);
        when(restaurant.getId()).thenReturn(10L);
        when(review.getDish()).thenReturn(dish);
        when(dish.getId()).thenReturn(20L);
        when(review.getCustomer()).thenReturn(customer);
        when(customer.getId()).thenReturn(30L);
        when(customer.getName()).thenReturn("John");
        when(customer.getSurname()).thenReturn("Doe");
        when(customer.getAvatarUrl()).thenReturn("avatar.jpg");

        List<Review> reviews = Collections.singletonList(review);

        // Act
        List<ReviewResponseDTO> result = reviewMapper.reviewsToReviewResponseDTOs(reviews);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(LocalDateTime.of(2025, 1, 1, 0, 0), result.get(0).getCreatedAt());
        assertEquals("Test Title", result.get(0).getTitle());
        assertEquals("Test Description", result.get(0).getDescription());
        assertEquals(4, result.get(0).getRating());
        assertEquals(10L, result.get(0).getRestaurantId());
        assertEquals(20L, result.get(0).getDishId());
        assertEquals(30L, result.get(0).getCustomerId());
        assertEquals("John", result.get(0).getCustomerName());
        assertEquals("Doe", result.get(0).getCustomerSurname());
        assertEquals("avatar.jpg", result.get(0).getCustomerAvatarUrl());
    }
}