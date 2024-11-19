package com.example.foody.mapper.impl;

import com.example.foody.builder.CategoryBuilder;
import com.example.foody.dto.request.CategoryRequestDTO;
import com.example.foody.dto.response.CategoryResponseDTO;
import com.example.foody.mapper.CategoryMapper;
import com.example.foody.model.Category;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CategoryMapperImpl implements CategoryMapper {
    private final CategoryBuilder categoryBuilder;

    public CategoryMapperImpl(CategoryBuilder categoryBuilder) {
        this.categoryBuilder = categoryBuilder;
    }

    @Override
    public CategoryResponseDTO categoryToCategoryResponseDTO(Category category) {
        if (category == null) {
            return null;
        }

        CategoryResponseDTO categoryResponseDTO = new CategoryResponseDTO();

        categoryResponseDTO.setId(category.getId());
        categoryResponseDTO.setName(category.getName());

        return categoryResponseDTO;
    }

    @Override
    public Category categoryRequestDTOToCategory(CategoryRequestDTO categoryRequestDTO) {
        if (categoryRequestDTO == null) {
            return null;
        }

        return categoryBuilder
                .name(categoryRequestDTO.getName())
                .build();
    }

    @Override
    public List<CategoryResponseDTO> categoriesToCategoryResponseDTOs(List<Category> categories) {
        if (categories == null) {
            return null;
        }

        List<CategoryResponseDTO> list = new ArrayList<>(categories.size());
        categories.forEach(category -> list.add(categoryToCategoryResponseDTO(category)));

        return list;
    }
}
