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
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation of the {@link CategoryService} interface.
 * <p>
 * Provides methods to create, update, and delete {@link Category} objects.
 */
@Service
@Transactional(rollbackOn = Exception.class)
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final RestaurantRepository restaurantRepository;
    private final CategoryMapper categoryMapper;

    /**
     * {@inheritDoc}
     * <p>
     * This method persists a new {@link Category} entity to the database.
     *
     * @param categoryDTO the category request data transfer object
     * @return the saved category response data transfer object
     * @throws EntityDuplicateException if the category with the specified name already exists
     * @throws EntityCreationException if there is an error during the creation of the category
     */
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

    /**
     * {@inheritDoc}
     * <p>
     * This method retrieves all {@link Category} entities from the database.
     *
     * @return the list of category response data transfer objects
     */
    @Override
    public List<CategoryResponseDTO> findAll() {
        List<Category> categories = categoryRepository.findAll();
        return categoryMapper.categoriesToCategoryResponseDTOs(categories);
    }

    /**
     * {@inheritDoc}
     * <p>
     * This method retrieves a {@link Category} entity by its ID.
     *
     * @param id the ID of the category to retrieve
     * @return the category response data transfer object
     * @throws EntityNotFoundException if the category with the specified ID is not found
     */
    @Override
    public CategoryResponseDTO findById(long id) {
        Category category = categoryRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("category", "id", id));
        return categoryMapper.categoryToCategoryResponseDTO(category);
    }

    /**
     * {@inheritDoc}
     * <p>
     * This method adds a restaurant to a {@link Category} entity.
     *
     * @param id the ID of the category
     * @param restaurant the restaurant to add
     * @return the updated category
     * @throws EntityNotFoundException if the category with the specified ID is not found
     * @throws EntityCreationException if there is an error during the update of the category
     */
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

    /**
     * {@inheritDoc}
     * <p>
     * This method removes a {@link Category} entity by its ID.
     *
     * @param id the ID of the category to remove
     * @return true if the category was removed, false otherwise
     * @throws EntityNotFoundException if the category with the specified ID is not found
     * @throws EntityDeletionException if there is an error during the deletion of the category
     */
    @Override
    public boolean remove(long id) {
        Category category = categoryRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("category", "id", id));

        removeCategoryFromRestaurants(category);

        try {
            categoryRepository.delete(category);
        } catch (Exception e) {
            throw new EntityDeletionException("category", "id", id);
        }

        return true;
    }

    /**
     * Removes the category from all associated restaurants.
     * <p>
     * This method iterates through all restaurants associated with the category and removes the category from each restaurant.
     *
     * @param category the category to remove from restaurants
     */
    private void removeCategoryFromRestaurants(Category category) {
        category.getRestaurants().forEach(restaurant ->
                restaurant.getCategories().remove(category)
        );
        restaurantRepository.saveAll(category.getRestaurants());
    }
}