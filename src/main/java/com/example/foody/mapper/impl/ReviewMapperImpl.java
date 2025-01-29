package com.example.foody.mapper.impl;

import com.example.foody.builder.ReviewBuilder;
import com.example.foody.dto.request.ReviewRequestDTO;
import com.example.foody.dto.response.ReviewResponseDTO;
import com.example.foody.mapper.ReviewMapper;
import com.example.foody.model.Dish;
import com.example.foody.model.Restaurant;
import com.example.foody.model.Review;
import com.example.foody.model.user.CustomerUser;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the {@link ReviewMapper} interface.
 * <p>
 * Provides methods to convert between {@link Review} entities and DTOs.
 */
@Component
public class ReviewMapperImpl implements ReviewMapper {

    private final ReviewBuilder reviewBuilder;

    public ReviewMapperImpl(ReviewBuilder reviewBuilder) {
        this.reviewBuilder = reviewBuilder;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Converts a {@link Review} entity to a {@link ReviewResponseDTO}.
     *
     * @param review the Review entity to convert
     * @return the converted ReviewResponseDTO
     */
    @Override
    public ReviewResponseDTO reviewToReviewResponseDTO(Review review) {
        if (review == null) {
            return null;
        }

        ReviewResponseDTO reviewResponseDTO = new ReviewResponseDTO();

        reviewResponseDTO.setId(review.getId());
        reviewResponseDTO.setCreatedAt(review.getCreatedAt());
        reviewResponseDTO.setTitle(review.getTitle());
        reviewResponseDTO.setDescription(review.getDescription());
        reviewResponseDTO.setRating(review.getRating());
        reviewResponseDTO.setRestaurantId(reviewRestaurantId(review));
        reviewResponseDTO.setDishId(reviewDishId(review));
        reviewResponseDTO.setCustomerId(reviewCustomerId(review));
        reviewResponseDTO.setCustomerName(reviewCustomerName(review));
        reviewResponseDTO.setCustomerSurname(reviewCustomerSurname(review));
        reviewResponseDTO.setCustomerAvatarUrl(reviewCustomerAvatarUrl(review));

        return reviewResponseDTO;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Converts a {@link ReviewRequestDTO} to a {@link Review} entity.
     *
     * @param reviewRequestDTO the ReviewRequestDTO to convert
     * @return the converted Review entity
     */
    @Override
    public Review reviewRequestDTOToReview(ReviewRequestDTO reviewRequestDTO) {
        if (reviewRequestDTO == null) {
            return null;
        }

        return reviewBuilder
                .title(reviewRequestDTO.getTitle())
                .description(reviewRequestDTO.getDescription())
                .rating(reviewRequestDTO.getRating())
                .build();
    }

    /**
     * {@inheritDoc}
     * <p>
     * Converts a list of {@link Review} entities to a list of {@link ReviewResponseDTO} objects.
     *
     * @param reviews the list of Review entities to convert
     * @return the list of converted ReviewResponseDTO objects
     */
    @Override
    public List<ReviewResponseDTO> reviewsToReviewResponseDTOs(List<Review> reviews) {
        if (reviews == null) {
            return null;
        }

        List<ReviewResponseDTO> list = new ArrayList<>(reviews.size());
        reviews.forEach(review -> list.add(reviewToReviewResponseDTO(review)));

        return list;
    }

    /**
     * Retrieves the restaurant ID from a {@link Review} entity.
     *
     * @param review the Review entity
     * @return the restaurant ID, or null if not available
     */
    private Long reviewRestaurantId(Review review) {
        if (review == null) {
            return null;
        }
        Restaurant restaurant = review.getRestaurant();
        if (restaurant == null) {
            return null;
        }
        return restaurant.getId();
    }

    /**
     * Retrieves the dish ID from a {@link Review} entity.
     *
     * @param review the Review entity
     * @return the dish ID, or null if not available
     */
    private Long reviewDishId(Review review) {
        if (review == null) {
            return null;
        }
        Dish dish = review.getDish();
        if (dish == null) {
            return null;
        }
        return dish.getId();
    }

    /**
     * Retrieves the customer ID from a {@link Review} entity.
     *
     * @param review the Review entity
     * @return the customer ID, or null if not available
     */
    private Long reviewCustomerId(Review review) {
        if (review == null) {
            return null;
        }
        CustomerUser customerUser = review.getCustomer();
        if (customerUser == null) {
            return null;
        }
        return customerUser.getId();
    }

    /**
     * Retrieves the customer name from a {@link Review} entity.
     *
     * @param review the Review entity
     * @return the customer name, or null if not available
     */
    private String reviewCustomerName(Review review) {
        if (review == null) {
            return null;
        }
        CustomerUser customerUser = review.getCustomer();
        if (customerUser == null) {
            return null;
        }
        return customerUser.getName();
    }

    /**
     * Retrieves the customer surname from a {@link Review} entity.
     *
     * @param review the Review entity
     * @return the customer surname, or null if not available
     */
    private String reviewCustomerSurname(Review review) {
        if (review == null) {
            return null;
        }
        CustomerUser customerUser = review.getCustomer();
        if (customerUser == null) {
            return null;
        }
        return customerUser.getSurname();
    }

    /**
     * Retrieves the customer avatar URL from a {@link Review} entity.
     *
     * @param review the Review entity
     * @return the customer avatar URL, or null if not available
     */
    private String reviewCustomerAvatarUrl(Review review) {
        if (review == null) {
            return null;
        }
        CustomerUser customerUser = review.getCustomer();
        if (customerUser == null) {
            return null;
        }
        return customerUser.getAvatarUrl();
    }
}