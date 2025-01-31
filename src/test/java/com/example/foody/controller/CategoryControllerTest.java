package com.example.foody.controller;

import com.example.foody.TestDataUtil;
import com.example.foody.dto.request.CategoryRequestDTO;
import com.example.foody.dto.response.CategoryResponseDTO;
import com.example.foody.exceptions.entity.EntityNotFoundException;
import com.example.foody.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/**
 * Test class for the endpoints in the {@link CategoryController} class using mock services.
 */
@ExtendWith(MockitoExtension.class)
public class CategoryControllerTest {

    @InjectMocks
    private CategoryController categoryController;

    @Mock
    private CategoryService categoryService;

    @Test
    void saveCategoryWhenValidRequestReturnsCreatedResponse() {
        // Arrange
        CategoryRequestDTO requestDTO = TestDataUtil.createTestCategoryRequestDTO();
        CategoryResponseDTO responseDTO = TestDataUtil.createTestCategoryResponseDTO();

        when(categoryService.save(requestDTO)).thenReturn(responseDTO);

        // Act
        ResponseEntity<CategoryResponseDTO> response = categoryController.saveCategory(requestDTO);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(responseDTO, response.getBody());
    }

    @Test
    void getCategoriesWhenCalledReturnsOkResponse() {
        // Arrange
        List<CategoryResponseDTO> responseDTOs =
                Collections.singletonList(TestDataUtil.createTestCategoryResponseDTO());

        when(categoryService.findAll()).thenReturn(responseDTOs);

        // Act
        ResponseEntity<List<CategoryResponseDTO>> response = categoryController.getCategories();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDTOs, response.getBody());
    }

    @Test
    void getCategoryByIdWhenValidIdReturnsOkResponse() {
        // Arrange
        CategoryResponseDTO responseDTO = TestDataUtil.createTestCategoryResponseDTO();

        when(categoryService.findById(1L)).thenReturn(responseDTO);

        // Act
        ResponseEntity<CategoryResponseDTO> response = categoryController.getCategoryById(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDTO, response.getBody());
    }

    @Test
    void getCategoryByIdWhenInvalidIdThrowsEntityNotFoundException() {
        // Arrange
        when(categoryService.findById(1L)).thenThrow(new EntityNotFoundException("Category", "id", 1L));

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> categoryController.getCategoryById(1L));
    }

    @Test
    void removeCategoryWhenValidIdRemovesCategory() {
        // Arrange
        when(categoryService.remove(1L)).thenReturn(true);

        // Act
        ResponseEntity<Void> response = categoryController.removeCategory(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(categoryService, times(1)).remove(1L);
    }
}