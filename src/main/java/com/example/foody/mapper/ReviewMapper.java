package com.example.foody.mapper;

import com.example.foody.dto.request.ReviewRequestDTO;
import com.example.foody.dto.response.ReviewResponseDTO;
import com.example.foody.model.Review;

import java.util.List;

/**
 * Mapper interface for converting between Review entities and DTOs.
 */
public interface ReviewMapper {

    /**
     * Converts a Review entity to a ReviewResponseDTO.
     *
     * @param review the Review entity to convert
     * @return the converted ReviewResponseDTO
     */
    ReviewResponseDTO reviewToReviewResponseDTO(Review review);

    /**
     * Converts a ReviewRequestDTO to a Review entity.
     *
     * @param reviewRequestDTO the ReviewRequestDTO to convert
     * @return the converted Review entity
     */
    Review reviewRequestDTOToReview(ReviewRequestDTO reviewRequestDTO);

    /**
     * Converts a list of Review entities to a list of ReviewResponseDTOs.
     *
     * @param reviews the list of Review entities to convert
     * @return the list of converted ReviewResponseDTOs
     */
    List<ReviewResponseDTO> reviewsToReviewResponseDTOs(List<Review> reviews);
}