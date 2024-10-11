package com.example.foody.service.impl;

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
import com.example.foody.service.CategoryService;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional(rollbackOn = Exception.class)
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final RestaurantRepository restaurantRepository;
    private final CategoryMapper categoryMapper;

    public CategoryServiceImpl(CategoryRepository categoryRepository, RestaurantRepository restaurantRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.restaurantRepository = restaurantRepository;
        this.categoryMapper = categoryMapper;
    }

    @Override
    public CategoryResponseDTO save(CategoryRequestDTO categoryDTO) {
        Category category = categoryMapper.categoryRequestDTOToCategory(categoryDTO);

        try {
            category = categoryRepository.save(category);
        } catch (DataIntegrityViolationException e) {
            throw new EntityDuplicateException("category", "name", categoryDTO.getName());
        } catch (Exception e) {
            throw new EntityCreationException("category");
        }

        return categoryMapper.categoryToCategoryResponseDTO(category);
    }

    @Override
    public List<CategoryResponseDTO> findAll() {
        List<Category> categories = categoryRepository.findAll();
        return categoryMapper.categoriesToCategoryResponseDTOs(categories);
    }

    @Override
    public CategoryResponseDTO findById(long id) {
        Category category = categoryRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("category", "id", id));
        return categoryMapper.categoryToCategoryResponseDTO(category);
    }

    @Override
    public Category addRestaurant(long id, Restaurant restaurant) {
        Category category = categoryRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("category", "id", id));

        category.getRestaurants().add(restaurant);

        try {
            return categoryRepository.save(category);
        } catch (Exception e) {
            throw new EntityCreationException("category");
        }
    }

    @Override
    public boolean remove(long id) {
        Category category = categoryRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("category", "id", id));

        // I remove the category from the restaurants
        category.getRestaurants().forEach(
                restaurant -> restaurant.getCategories().remove(category)
        );

        restaurantRepository.saveAll(category.getRestaurants());

        try {
            categoryRepository.delete(category);
        } catch (Exception e) {
            throw new EntityDeletionException("category", "id", id);
        }

        return true;
    }
}
