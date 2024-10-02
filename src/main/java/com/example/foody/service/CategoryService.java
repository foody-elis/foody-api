package com.example.foody.service;

import com.example.foody.dto.request.CategoryRequestDTO;
import com.example.foody.dto.response.CategoryResponseDTO;
import com.example.foody.model.Category;
import com.example.foody.model.Restaurant;

import java.util.List;

public interface CategoryService {
    CategoryResponseDTO save(CategoryRequestDTO categoryDTO);
    List<CategoryResponseDTO> findAll();
    CategoryResponseDTO findById(long id);
    Category addRestaurant(long id, Restaurant restaurant);
    boolean remove(long id);
}
