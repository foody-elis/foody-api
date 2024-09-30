package com.example.foody.mapper;

import com.example.foody.dto.request.CategoryRequestDTO;
import com.example.foody.dto.response.CategoryResponseDTO;
import com.example.foody.model.Category;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryResponseDTO categoryToCategoryResponseDTO(Category category);
    Category categoryRequestDTOToCategory(CategoryRequestDTO categoryRequestDTO);
    List<CategoryResponseDTO> categoriesToCategoryResponseDTOs(List<Category> categories);
}
