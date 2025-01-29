package com.example.foody.mapper;

import com.example.foody.dto.request.CategoryRequestDTO;
import com.example.foody.dto.response.CategoryResponseDTO;
import com.example.foody.model.Category;

import java.util.List;

/**
 * Mapper interface for converting between Category entities and DTOs.
 */
public interface CategoryMapper {

    /**
     * Converts a Category entity to a CategoryResponseDTO.
     *
     * @param category the Category entity to convert
     * @return the converted CategoryResponseDTO
     */
    CategoryResponseDTO categoryToCategoryResponseDTO(Category category);

    /**
     * Converts a CategoryRequestDTO to a Category entity.
     *
     * @param categoryRequestDTO the CategoryRequestDTO to convert
     * @return the converted Category entity
     */
    Category categoryRequestDTOToCategory(CategoryRequestDTO categoryRequestDTO);

    /**
     * Converts a list of Category entities to a list of CategoryResponseDTOs.
     *
     * @param categories the list of Category entities to convert
     * @return the list of converted CategoryResponseDTOs
     */
    List<CategoryResponseDTO> categoriesToCategoryResponseDTOs(List<Category> categories);
}