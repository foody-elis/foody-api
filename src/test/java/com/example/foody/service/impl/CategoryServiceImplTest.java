package com.example.foody.service.impl;

import com.example.foody.TestDataUtil;
import com.example.foody.dto.request.CategoryRequestDTO;
import com.example.foody.dto.response.CategoryResponseDTO;
import com.example.foody.exceptions.entity.EntityCreationException;
import com.example.foody.exceptions.entity.EntityDeletionException;
import com.example.foody.exceptions.entity.EntityDuplicateException;
import com.example.foody.exceptions.entity.EntityNotFoundException;
import com.example.foody.mapper.CategoryMapper;
import com.example.foody.model.Category;
import com.example.foody.model.Restaurant;
import com.example.foody.repository.CategoryRepository;
import com.example.foody.repository.RestaurantRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceImplTest {
    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @Test
    void saveWhenCategoryIsValidReturnsCategoryResponseDTO() {
        // Arrange
        CategoryRequestDTO requestDTO = TestDataUtil.createTestCategoryRequestDTO();
        Category category = TestDataUtil.createTestCategory();

        when(categoryMapper.categoryRequestDTOToCategory(requestDTO)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.categoryToCategoryResponseDTO(category))
                .thenReturn(TestDataUtil.createTestCategoryResponseDTO());

        // Act
        CategoryResponseDTO responseDTO = categoryService.save(requestDTO);

        // Assert
        assertNotNull(responseDTO);
        verify(categoryRepository, times(1)).save(category);
    }

    @Test
    void saveWhenDuplicateCategoryThrowsEntityDuplicateException() {
        // Arrange
        CategoryRequestDTO requestDTO = TestDataUtil.createTestCategoryRequestDTO();
        Category category = TestDataUtil.createTestCategory();

        when(categoryMapper.categoryRequestDTOToCategory(requestDTO)).thenReturn(category);
        doThrow(DataIntegrityViolationException.class).when(categoryRepository).save(category);

        // Act & Assert
        assertThrows(EntityDuplicateException.class, () -> categoryService.save(requestDTO));
        verify(categoryRepository, times(1)).save(category);
    }

    @Test
    void saveWhenSaveFailsThrowsEntityCreationException() {
        // Arrange
        CategoryRequestDTO requestDTO = TestDataUtil.createTestCategoryRequestDTO();
        Category category = TestDataUtil.createTestCategory();

        when(categoryMapper.categoryRequestDTOToCategory(requestDTO)).thenReturn(category);
        doThrow(new RuntimeException()).when(categoryRepository).save(category);

        // Act & Assert
        assertThrows(EntityCreationException.class, () -> categoryService.save(requestDTO));
        verify(categoryRepository, times(1)).save(category);
    }

    @Test
    void findAllReturnsListOfCategoryResponseDTOs() {
        // Arrange
        List<Category> categories = List.of(TestDataUtil.createTestCategory());

        when(categoryRepository.findAll()).thenReturn(categories);
        when(categoryMapper.categoriesToCategoryResponseDTOs(categories))
                .thenReturn(List.of(TestDataUtil.createTestCategoryResponseDTO()));

        // Act
        List<CategoryResponseDTO> responseDTOs = categoryService.findAll();

        // Assert
        assertNotNull(responseDTOs);
        assertEquals(1, responseDTOs.size());
        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    void findByIdWhenCategoryExistsReturnsCategoryResponseDTO() {
        // Arrange
        Category category = TestDataUtil.createTestCategory();

        when(categoryRepository.findById(category.getId())).thenReturn(Optional.of(category));
        when(categoryMapper.categoryToCategoryResponseDTO(category))
                .thenReturn(TestDataUtil.createTestCategoryResponseDTO());

        // Act
        CategoryResponseDTO responseDTO = categoryService.findById(category.getId());

        // Assert
        assertNotNull(responseDTO);
        verify(categoryRepository, times(1)).findById(category.getId());
    }

    @Test
    void findByIdWhenCategoryDoesNotExistThrowsEntityNotFoundException() {
        // Arrange
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> categoryService.findById(0L));
        verify(categoryRepository, times(1)).findById(0L);
    }

    @Test
    void addRestaurantWhenCategoryExistsAddsRestaurant() {
        // Arrange
        Category category = TestDataUtil.createTestCategory();
        Restaurant restaurant = TestDataUtil.createTestRestaurant();

        when(categoryRepository.findById(category.getId())).thenReturn(Optional.of(category));
        when(categoryRepository.save(category)).thenReturn(category);

        // Act
        Category updatedCategory = categoryService.addRestaurant(category.getId(), restaurant);

        // Assert
        assertNotNull(updatedCategory);
        assertTrue(updatedCategory.getRestaurants().contains(restaurant));
        verify(categoryRepository, times(1)).findById(category.getId());
        verify(categoryRepository, times(1)).save(category);
    }

    @Test
    void addRestaurantWhenCategoryDoesNotExistThrowsEntityNotFoundException() {
        // Arrange
        Restaurant restaurant = TestDataUtil.createTestRestaurant();

        when(categoryRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> categoryService.addRestaurant(0L, restaurant));
        verify(categoryRepository, times(1)).findById(0L);
    }

    @Test
    void addRestaurantWhenSaveFailsThrowsEntityCreationException() {
        // Arrange
        Category category = TestDataUtil.createTestCategory();
        Restaurant restaurant = TestDataUtil.createTestRestaurant();

        when(categoryRepository.findById(category.getId())).thenReturn(Optional.of(category));
        when(categoryRepository.save(category)).thenThrow(new RuntimeException());

        // Act & Assert
        assertThrows(EntityCreationException.class, () -> categoryService.addRestaurant(category.getId(), restaurant));
        verify(categoryRepository, times(1)).findById(category.getId());
        verify(categoryRepository, times(1)).save(category);
    }

    @Test
    void removeWhenCategoryExistsDeletesCategory() {
        // Arrange
        Category category = TestDataUtil.createTestCategory();
        Restaurant restaurant = TestDataUtil.createTestRestaurant();
        category.getRestaurants().add(restaurant);
        restaurant.getCategories().add(category);

        when(categoryRepository.findById(category.getId())).thenReturn(Optional.of(category));

        // Act
        boolean result = categoryService.remove(category.getId());

        // Assert
        assertTrue(result);
        verify(categoryRepository, times(1)).findById(category.getId());
        verify(categoryRepository, times(1)).delete(category);
    }

    @Test
    void removeWhenCategoryDoesNotExistThrowsEntityNotFoundException() {
        // Arrange
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> categoryService.remove(0L));
        verify(categoryRepository, times(1)).findById(0L);
        verify(categoryRepository, never()).delete(any(Category.class));
    }

    @Test
    void removeWhenDeleteFailsThrowsEntityDeletionException() {
        // Arrange
        Category category = TestDataUtil.createTestCategory();

        when(categoryRepository.findById(category.getId())).thenReturn(Optional.of(category));
        doThrow(RuntimeException.class).when(categoryRepository).delete(category);

        // Act & Assert
        assertThrows(EntityDeletionException.class, () -> categoryService.remove(category.getId()));
        verify(categoryRepository, times(1)).findById(category.getId());
        verify(categoryRepository, times(1)).delete(category);
    }
}