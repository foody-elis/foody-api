package com.example.foody.mapper.impl;

import com.example.foody.TestDataUtil;
import com.example.foody.builder.CategoryBuilder;
import com.example.foody.dto.request.CategoryRequestDTO;
import com.example.foody.dto.response.CategoryResponseDTO;
import com.example.foody.model.Category;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryMapperImplTest {

    @InjectMocks
    private CategoryMapperImpl categoryMapper;

    @Mock
    private CategoryBuilder categoryBuilder;

    @Test
    void categoryToCategoryResponseDTOWhenCategoryIsNullReturnsNull() {
        // Act
        CategoryResponseDTO result = categoryMapper.categoryToCategoryResponseDTO(null);

        // Assert
        assertNull(result);
    }

    @Test
    void categoryToCategoryResponseDTOWhenValidReturnsDTO() {
        // Arrange
        Category category = TestDataUtil.createTestCategory();

        // Act
        CategoryResponseDTO result = categoryMapper.categoryToCategoryResponseDTO(category);

        // Assert
        assertNotNull(result);
        assertEquals(category.getId(), result.getId());
        assertEquals(category.getName(), result.getName());
    }

    @Test
    void categoryRequestDTOToCategoryWhenRequestIsNullReturnsNull() {
        // Act
        Category result = categoryMapper.categoryRequestDTOToCategory(null);

        // Assert
        assertNull(result);
    }

    @Test
    void categoryRequestDTOToCategoryWhenValidReturnsCategory() {
        // Arrange
        CategoryRequestDTO requestDTO = mock(CategoryRequestDTO.class);
        when(requestDTO.getName()).thenReturn("Test Category");

        Category category = mock(Category.class);
        when(categoryBuilder.name("Test Category")).thenReturn(categoryBuilder);
        when(categoryBuilder.build()).thenReturn(category);

        // Act
        Category result = categoryMapper.categoryRequestDTOToCategory(requestDTO);

        // Assert
        assertNotNull(result);
        verify(categoryBuilder).name("Test Category");
        verify(categoryBuilder).build();
    }

    @Test
    void categoriesToCategoryResponseDTOsWhenCategoriesIsNullReturnsNull() {
        // Act
        List<CategoryResponseDTO> result = categoryMapper.categoriesToCategoryResponseDTOs(null);

        // Assert
        assertNull(result);
    }

    @Test
    void categoriesToCategoryResponseDTOsWhenValidReturnsDTOList() {
        // Arrange
        Category category = mock(Category.class);
        when(category.getId()).thenReturn(1L);
        when(category.getName()).thenReturn("Test Category");

        List<Category> categories = Collections.singletonList(category);

        // Act
        List<CategoryResponseDTO> result = categoryMapper.categoriesToCategoryResponseDTOs(categories);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals("Test Category", result.get(0).getName());
    }
}