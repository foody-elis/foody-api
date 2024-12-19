package com.example.foody.mapper;

import com.example.foody.dto.request.ReviewRequestDTO;
import com.example.foody.dto.response.ReviewResponseDTO;
import com.example.foody.model.Review;

import java.util.List;

public interface ReviewMapper {
    ReviewResponseDTO reviewToReviewResponseDTO(Review review);
    Review reviewRequestDTOToReview(ReviewRequestDTO reviewRequestDTO);
    List<ReviewResponseDTO> reviewsToReviewResponseDTOs(List<Review> reviews);
}