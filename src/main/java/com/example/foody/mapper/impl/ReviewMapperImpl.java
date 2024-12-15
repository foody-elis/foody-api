package com.example.foody.mapper.impl;

import com.example.foody.dto.response.ReviewResponseDTO;
import com.example.foody.mapper.ReviewMapper;
import com.example.foody.model.Dish;
import com.example.foody.model.Restaurant;
import com.example.foody.model.Review;
import com.example.foody.model.user.CustomerUser;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ReviewMapperImpl implements ReviewMapper {

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

    @Override
    public List<ReviewResponseDTO> reviewsToReviewResponseDTOs(List<Review> reviews) {
        if (reviews == null) {
            return null;
        }

        List<ReviewResponseDTO> list = new ArrayList<>(reviews.size());
        reviews.forEach(review -> list.add(reviewToReviewResponseDTO(review)));

        return list;
    }

    private long reviewRestaurantId(Review review) {
        if (review == null) {
            return 0L;
        }
        Restaurant restaurant = review.getRestaurant();
        if (restaurant == null) {
            return 0L;
        }
        return restaurant.getId();
    }

    private long reviewDishId(Review review) {
        if (review == null) {
            return 0L;
        }
        Dish dish = review.getDish();
        if (dish == null) {
            return 0L;
        }
        return dish.getId();
    }

    private long reviewCustomerId(Review review) {
        if (review == null) {
            return 0L;
        }
        CustomerUser customerUser = review.getCustomer();
        if (customerUser == null) {
            return 0L;
        }
        return customerUser.getId();
    }

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