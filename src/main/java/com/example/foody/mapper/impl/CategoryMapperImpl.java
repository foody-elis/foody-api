package com.example.foody.mapper.impl;

import com.example.foody.builder.CategoryBuilder;
import com.example.foody.dto.request.CategoryRequestDTO;
import com.example.foody.dto.response.CategoryResponseDTO;
import com.example.foody.mapper.CategoryMapper;
import com.example.foody.model.Category;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the {@link CategoryMapper} interface.
 * <p>
 * Provides methods to convert between {@link Category} entities and DTOs.
 */
@Component
@AllArgsConstructor
public class CategoryMapperImpl implements CategoryMapper {

    private final CategoryBuilder categoryBuilder;

    /**
     * {@inheritDoc}
     * <p>
     * Converts a {@link Category} entity to a {@link CategoryResponseDTO}.
     *
     * @param category the Category entity to convert
     * @return the converted CategoryResponseDTO
     */
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

    /**
     * {@inheritDoc}
     * <p>
     * Converts a {@link CategoryRequestDTO} to a {@link Category} entity.
     *
     * @param categoryRequestDTO the CategoryRequestDTO to convert
     * @return the converted Category entity
     */
    @Override
    public Category categoryRequestDTOToCategory(CategoryRequestDTO categoryRequestDTO) {
        if (categoryRequestDTO == null) {
            return null;
        }

        return categoryBuilder
                .name(categoryRequestDTO.getName())
                .build();
    }

    /**
     * {@inheritDoc}
     * <p>
     * Converts a list of {@link Category} entities to a list of {@link CategoryResponseDTO} objects.
     *
     * @param categories the list of Category entities to convert
     * @return the list of converted CategoryResponseDTO objects
     */
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