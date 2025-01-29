package com.example.foody.service;

import com.example.foody.dto.request.ReviewRequestDTO;
import com.example.foody.dto.response.ReviewResponseDTO;

import java.util.List;

public interface ReviewService {
    ReviewResponseDTO save(ReviewRequestDTO reviewDTO);

    List<ReviewResponseDTO> findAll();

    ReviewResponseDTO findById(long id);

    List<ReviewResponseDTO> findAllByCustomer(long customerId);

    List<ReviewResponseDTO> findAllByRestaurant(long restaurantId);

    List<ReviewResponseDTO> findAllByDish(long dishId);

    boolean remove(long id);
}