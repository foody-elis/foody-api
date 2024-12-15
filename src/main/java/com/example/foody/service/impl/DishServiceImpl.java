package com.example.foody.service.impl;

import com.example.foody.dto.request.DishRequestDTO;
import com.example.foody.dto.request.DishUpdateRequestDTO;
import com.example.foody.dto.response.DishResponseDTO;
import com.example.foody.exceptions.entity.EntityCreationException;
import com.example.foody.exceptions.entity.EntityDeletionException;
import com.example.foody.exceptions.entity.EntityEditException;
import com.example.foody.exceptions.entity.EntityNotFoundException;
import com.example.foody.exceptions.restaurant.ForbiddenRestaurantAccessException;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(rollbackOn = Exception.class)
public class DishServiceImpl implements DishService {
    private final DishRepository dishRepository;
    private final RestaurantRepository restaurantRepository;
    private final DishMapper dishMapper;
    private final GoogleDriveService googleDriveService;

    public DishServiceImpl(DishRepository dishRepository, RestaurantRepository restaurantRepository, DishMapper dishMapper, GoogleDriveService googleDriveService) {
        this.dishRepository = dishRepository;
        this.restaurantRepository = restaurantRepository;
        this.dishMapper = dishMapper;
        this.googleDriveService = googleDriveService;
    }

    @Override
    public DishResponseDTO save(DishRequestDTO dishDTO) {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Dish dish = dishMapper.dishRequestDTOToDish(dishDTO);
        Restaurant restaurant = restaurantRepository
                .findByIdAndDeletedAtIsNull(dishDTO.getRestaurantId()) // Dishes can also be saved if the restaurant is not approved
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

        return dishMapper.dishToDishResponseDTO(dish);
    }

    @Override
    public List<DishResponseDTO> findAll() {
        List<Dish> dishes = dishRepository.findAllByDeletedAtIsNull();
        return dishMapper.dishesToDishResponseDTOs(dishes);
    }

    @Override
    public DishResponseDTO findById(long id) {
        Dish dish = dishRepository
                .findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new EntityNotFoundException("dish", "id", id));
        return dishMapper.dishToDishResponseDTO(dish);
    }

    @Override
    public List<DishResponseDTO> findAllByRestaurant(long restaurantId) {
        List<Dish> dishes = dishRepository
                .findAllByDeletedAtIsNullAndRestaurant_Id(restaurantId);
        return dishMapper.dishesToDishResponseDTOs(dishes);
    }

    @Override
    public DishResponseDTO update(long id, DishUpdateRequestDTO dishDTO) {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Dish dish = dishRepository
                .findByIdAndDeletedAtIsNull(id)
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

        return dishMapper.dishToDishResponseDTO(dish);
    }

    @Override
    public boolean remove(long id) {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Dish dish = dishRepository
                .findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new EntityNotFoundException("dish", "id", id));
        dish.delete();

        checkDishAccessOrThrow(principal, dish);
        googleDriveService.deleteImage(dish.getPhotoUrl());

        try {
            dishRepository.save(dish);
        } catch (Exception e) {
            throw new EntityDeletionException("dish", "id", id);
        }

        return true;
    }

    private void checkDishCreationOrThrow(User user, Restaurant restaurant) {
        if (restaurant.getRestaurateur().getId() == user.getId()) return;
        if (UserRoleUtils.isAdmin(user)) return;

        throw new ForbiddenRestaurantAccessException();
    }

    private String saveDishPhoto(String dishPhotoBase64) {
        return Optional.ofNullable(dishPhotoBase64)
                .map(photoBase64 -> googleDriveService.uploadBase64Image(photoBase64, GoogleDriveFileType.DISH_PHOTO))
                .orElse(null);
    }

    private void checkDishAccessOrThrow(User user, Dish dish) {
        if (dish.getRestaurant().getRestaurateur().getId() == user.getId()) return;
        if (UserRoleUtils.isAdmin(user)) return;

        throw new ForbiddenRestaurantAccessException();
    }

    private String updateDishPhoto(Dish dish, String photoBase64) {
        removeDishPhoto(dish);
        return saveDishPhoto(photoBase64);
    }

    private void removeDishPhoto(Dish dish) {
        Optional.ofNullable(dish.getPhotoUrl())
                .ifPresent(googleDriveService::deleteImage);
    }
}