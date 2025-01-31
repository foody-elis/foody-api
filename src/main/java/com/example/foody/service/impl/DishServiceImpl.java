package com.example.foody.service.impl;

import com.example.foody.dto.request.DishRequestDTO;
import com.example.foody.dto.request.DishUpdateRequestDTO;
import com.example.foody.dto.response.DishResponseDTO;
import com.example.foody.exceptions.dish.ForbiddenDishAccessException;
import com.example.foody.exceptions.entity.EntityCreationException;
import com.example.foody.exceptions.entity.EntityDeletionException;
import com.example.foody.exceptions.entity.EntityEditException;
import com.example.foody.exceptions.entity.EntityNotFoundException;
import com.example.foody.exceptions.restaurant.ForbiddenRestaurantAccessException;
import com.example.foody.helper.DishHelper;
import com.example.foody.mapper.DishMapper;
import com.example.foody.model.Dish;
import com.example.foody.model.Restaurant;
import com.example.foody.model.user.User;
import com.example.foody.repository.DishRepository;
import com.example.foody.repository.RestaurantRepository;
import com.example.foody.service.DishService;
import com.example.foody.service.GoogleDriveService;
import com.example.foody.utils.UserRoleUtils;
import com.example.foody.utils.enums.GoogleDriveFileType;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of the {@link DishService} interface.
 * <p>
 * Provides methods to create, update, and delete {@link Dish} objects.
 */
@Service
@Transactional(rollbackOn = Exception.class)
@AllArgsConstructor
public class DishServiceImpl implements DishService {

    private final DishRepository dishRepository;
    private final RestaurantRepository restaurantRepository;
    private final DishMapper dishMapper;
    private final DishHelper dishHelper;
    private final GoogleDriveService googleDriveService;

    /**
     * {@inheritDoc}
     * <p>
     * This method persists a new {@link Dish} entity to the database.
     *
     * @param dishDTO the dish request data transfer object
     * @return the saved dish response data transfer object
     * @throws EntityCreationException if there is an error during the creation of the dish
     * @throws EntityNotFoundException if the restaurant is not found
     */
    @Override
    public DishResponseDTO save(DishRequestDTO dishDTO) {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Dish dish = dishMapper.dishRequestDTOToDish(dishDTO);
        Restaurant restaurant = restaurantRepository
                .findById(dishDTO.getRestaurantId()) // Dishes can also be saved if the restaurant is not approved
                .orElseThrow(() -> new EntityNotFoundException("restaurant", "id", dishDTO.getRestaurantId()));

        checkDishCreationOrThrow(principal, restaurant);

        String photoUrl = saveDishPhoto(dishDTO.getPhotoBase64());

        dish.setRestaurant(restaurant);
        dish.setPhotoUrl(photoUrl);

        try {
            dish = dishRepository.save(dish);
        } catch (Exception e) {
            removeDishPhoto(dish);
            throw new EntityCreationException("dish");
        }

        return dishHelper.buildDishResponseDTO(dish);
    }

    /**
     * {@inheritDoc}
     * <p>
     * This method retrieves all {@link Dish} entities from the database.
     *
     * @return the list of dish response data transfer objects
     */
    @Override
    public List<DishResponseDTO> findAll() {
        List<Dish> dishes = dishRepository.findAll();
        return dishHelper.buildDishResponseDTOs(dishes);
    }

    /**
     * {@inheritDoc}
     * <p>
     * This method retrieves a {@link Dish} entity by its ID.
     *
     * @param id the ID of the dish to retrieve
     * @return the dish response data transfer object
     * @throws EntityNotFoundException if the dish with the specified ID is not found
     */
    @Override
    public DishResponseDTO findById(long id) {
        Dish dish = dishRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("dish", "id", id));
        return dishHelper.buildDishResponseDTO(dish);
    }

    /**
     * {@inheritDoc}
     * <p>
     * This method retrieves all {@link Dish} entities for a specific restaurant.
     *
     * @param restaurantId the ID of the restaurant
     * @return the list of dish response data transfer objects
     */
    @Override
    public List<DishResponseDTO> findAllByRestaurant(long restaurantId) {
        List<Dish> dishes = dishRepository
                .findAllByRestaurant_Id(restaurantId);
        return dishHelper.buildDishResponseDTOs(dishes);
    }

    /**
     * {@inheritDoc}
     * <p>
     * This method updates a {@link Dish} entity by its ID.
     *
     * @param id the ID of the dish to update
     * @param dishDTO the dish update request data transfer object
     * @return the updated dish response data transfer object
     * @throws EntityNotFoundException if the dish with the specified ID is not found
     * @throws EntityEditException if there is an error during the update of the dish
     */
    @Override
    public DishResponseDTO update(long id, DishUpdateRequestDTO dishDTO) {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Dish dish = dishRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("dish", "id", id));

        checkDishAccessOrThrow(principal, dish);

        String updatedPhotoUrl = updateDishPhoto(dish, dishDTO.getPhotoBase64());

        dishMapper.updateDishFromDishUpdateRequestDTO(dish, dishDTO);
        dish.setPhotoUrl(updatedPhotoUrl);

        try {
            dish = dishRepository.save(dish);
        } catch (Exception e) {
            throw new EntityEditException("dish", "id", id);
        }

        return dishHelper.buildDishResponseDTO(dish);
    }

    /**
     * {@inheritDoc}
     * <p>
     * This method removes a {@link Dish} entity by its ID.
     *
     * @param id the ID of the dish to remove
     * @return true if the dish was removed, false otherwise
     * @throws EntityNotFoundException if the dish with the specified ID is not found
     * @throws EntityDeletionException if there is an error during the deletion of the dish
     */
    @Override
    public boolean remove(long id) {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Dish dish = dishRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("dish", "id", id));
        dish.delete();

        checkDishAccessOrThrow(principal, dish);
        removeDishPhoto(dish);

        try {
            dishRepository.save(dish);
        } catch (Exception e) {
            throw new EntityDeletionException("dish", "id", id);
        }

        return true;
    }

    /**
     * Checks if the user has access to create a dish for the restaurant.
     * <p>
     * This method throws a {@link ForbiddenRestaurantAccessException} if the user does not have access to the restaurant.
     *
     * @param user the user to check
     * @param restaurant the restaurant to check access for
     * @throws ForbiddenRestaurantAccessException if access to the restaurant is forbidden
     */
    private void checkDishCreationOrThrow(User user, Restaurant restaurant) {
        if (!UserRoleUtils.isRestaurateur(user)) return;
        if (restaurant.getRestaurateur().getId() == user.getId()) return;

        throw new ForbiddenRestaurantAccessException();
    }

    /**
     * Saves the dish photo.
     * <p>
     * This method uploads the base64 encoded dish photo to Google Drive and returns the URL.
     *
     * @param dishPhotoBase64 the base64 encoded dish photo
     * @return the URL of the uploaded dish photo
     */
    private String saveDishPhoto(String dishPhotoBase64) {
        return Optional.ofNullable(dishPhotoBase64)
                .map(photoBase64 -> googleDriveService.uploadBase64Image(photoBase64, GoogleDriveFileType.DISH_PHOTO))
                .orElse(null);
    }

    /**
     * Checks if the user has access to the dish.
     * <p>
     * This method throws a {@link ForbiddenDishAccessException} if the user does not have access to the dish.
     *
     * @param user the user to check
     * @param dish the dish to check access for
     * @throws ForbiddenDishAccessException if access to the dish is forbidden
     */
    private void checkDishAccessOrThrow(User user, Dish dish) {
        if (!UserRoleUtils.isRestaurateur(user)) return;
        if (dish.getRestaurant().getRestaurateur().getId() == user.getId()) return;

        throw new ForbiddenDishAccessException();
    }

    /**
     * Updates the dish photo.
     * <p>
     * This method removes the old dish photo and uploads the new base64 encoded dish photo to Google Drive.
     *
     * @param dish the dish to update the photo for
     * @param photoBase64 the base64 encoded new dish photo
     * @return the URL of the uploaded new dish photo
     */
    private String updateDishPhoto(Dish dish, String photoBase64) {
        removeDishPhoto(dish);
        return saveDishPhoto(photoBase64);
    }

    /**
     * Removes the dish photo.
     * <p>
     * This method deletes the dish photo from Google Drive.
     *
     * @param dish the dish whose photo to remove
     */
    private void removeDishPhoto(Dish dish) {
        Optional.ofNullable(dish.getPhotoUrl())
                .ifPresent(googleDriveService::deleteImage);
    }
}