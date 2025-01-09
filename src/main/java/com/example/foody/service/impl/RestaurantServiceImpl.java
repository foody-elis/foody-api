package com.example.foody.service.impl;

import com.example.foody.dto.request.RestaurantRequestDTO;
import com.example.foody.dto.response.DetailedRestaurantResponseDTO;
import com.example.foody.dto.response.RestaurantResponseDTO;
import com.example.foody.exceptions.entity.EntityCreationException;
import com.example.foody.exceptions.entity.EntityDeletionException;
import com.example.foody.exceptions.entity.EntityEditException;
import com.example.foody.exceptions.entity.EntityNotFoundException;
import com.example.foody.exceptions.restaurant.ForbiddenRestaurantAccessException;
import com.example.foody.exceptions.restaurant.RestaurateurAlreadyHasRestaurantException;
import com.example.foody.helper.RestaurantHelper;
import com.example.foody.mapper.RestaurantMapper;
import com.example.foody.model.Address;
import com.example.foody.model.Category;
import com.example.foody.model.Restaurant;
import com.example.foody.model.user.RestaurateurUser;
import com.example.foody.model.user.User;
import com.example.foody.repository.CategoryRepository;
import com.example.foody.repository.RestaurantRepository;
import com.example.foody.service.*;
import com.example.foody.utils.UserRoleUtils;
import com.example.foody.utils.enums.EmailPlaceholder;
import com.example.foody.utils.enums.EmailTemplateType;
import com.example.foody.utils.enums.GoogleDriveFileType;
import jakarta.transaction.Transactional;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional(rollbackOn = Exception.class)
public class RestaurantServiceImpl implements RestaurantService {
    private final RestaurantRepository restaurantRepository;
    private final CategoryRepository categoryRepository;
    private final RestaurantMapper restaurantMapper;
    private final RestaurantHelper restaurantHelper;
    private final CategoryService categoryService;
    private final AddressService addressService;
    private final GoogleDriveService googleDriveService;
    private final EmailService emailService;

    public RestaurantServiceImpl(RestaurantRepository restaurantRepository, CategoryRepository categoryRepository, RestaurantMapper restaurantMapper, RestaurantHelper restaurantHelper, CategoryService categoryService, AddressService addressService, GoogleDriveService googleDriveService, EmailService emailService) {
        this.restaurantRepository = restaurantRepository;
        this.categoryRepository = categoryRepository;
        this.restaurantMapper = restaurantMapper;
        this.restaurantHelper = restaurantHelper;
        this.categoryService = categoryService;
        this.addressService = addressService;
        this.googleDriveService = googleDriveService;
        this.emailService = emailService;
    }

    @Override
    public RestaurantResponseDTO save(RestaurantRequestDTO restaurantDTO) {
        RestaurateurUser principal = (RestaurateurUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Restaurant restaurant = restaurantMapper.restaurantRequestDTOToRestaurant(restaurantDTO);

        checkRestaurantCreationOrThrow(principal);

        Address address = saveRestaurantAddress(restaurant);
        List<Category> categories = addRestaurantToCategories(restaurant, restaurantDTO.getCategories());
        String photoUrl = saveRestaurantPhoto(restaurantDTO.getPhotoBase64());

        restaurant.setRestaurateur(principal);
        restaurant.setAddress(address);
        restaurant.setCategories(categories);
        restaurant.setPhotoUrl(photoUrl);

        try {
            restaurant = restaurantRepository.save(restaurant);
        } catch (Exception e) {
            removeRestaurantPhoto(restaurant);
            throw new EntityCreationException("restaurant");
        }

        sendRestaurantRegistrationEmail(restaurant);

        return restaurantMapper.restaurantToRestaurantResponseDTO(restaurant);
    }

    @Override
    public List<DetailedRestaurantResponseDTO> findAll() {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Restaurant> restaurants = getRestaurantsBasedOnUserRole(principal);
        return restaurantHelper.buildDetailedRestaurantResponseDTOs(restaurants);
    }

    @Override
    public DetailedRestaurantResponseDTO findById(long id) {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Restaurant restaurant = getRestaurantByIdBasedOnUserRole(principal, id);
        return restaurantHelper.buildDetailedRestaurantResponseDTO(restaurant);
    }

    @Override
    public DetailedRestaurantResponseDTO findByRestaurateur(long restaurateurId) {
        Restaurant restaurant = restaurantRepository
                .findAllByRestaurateur_Id(restaurateurId)
                .orElseThrow(() -> new EntityNotFoundException("restaurant", "restaurateurId", restaurateurId));
        return restaurantHelper.buildDetailedRestaurantResponseDTO(restaurant);
    }

    @Override
    public List<DetailedRestaurantResponseDTO> findAllByCategory(long categoryId) {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Restaurant> restaurants = getRestaurantByCategoryBasedOnUserRole(principal, categoryId);
        return restaurantHelper.buildDetailedRestaurantResponseDTOs(restaurants);
    }

    @Override
    public DetailedRestaurantResponseDTO approveById(long id) {
        Restaurant restaurant = restaurantRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("restaurant", "id", id));
        restaurant.setApproved(true);

        try {
            restaurant = restaurantRepository.save(restaurant);
        } catch (Exception e) {
            throw new EntityEditException("restaurant", "id", id);
        }

        sendRestaurantApprovalEmail(restaurant);

        return restaurantHelper.buildDetailedRestaurantResponseDTO(restaurant);
    }

    @Override
    public DetailedRestaurantResponseDTO update(long id, RestaurantRequestDTO restaurantDTO) {
        RestaurateurUser principal = (RestaurateurUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Restaurant restaurant = restaurantRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("restaurant", "id", id));

        checkRestaurantAccessOrThrow(principal, restaurant);

        Address updatedAddress = updateRestaurantAddress(restaurant, restaurantDTO);
        List<Category> updatedCategories = updateRestaurantCategories(restaurant, restaurantDTO.getCategories());
        String updatedPhotoUrl = updateRestaurantPhoto(restaurant, restaurantDTO.getPhotoBase64());

        restaurantMapper.updateRestaurantFromRestaurantRequestDTO(restaurant, restaurantDTO);
        restaurant.setAddress(updatedAddress);
        restaurant.setCategories(updatedCategories);
        restaurant.setPhotoUrl(updatedPhotoUrl);

        try {
            restaurant = restaurantRepository.save(restaurant);
        } catch (Exception e) {
            throw new EntityEditException("restaurant", "id", id);
        }

        return restaurantHelper.buildDetailedRestaurantResponseDTO(restaurant);
    }

    @Override
    public boolean remove(long id) {
        Restaurant restaurant = restaurantRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("restaurant", "id", id));
        restaurant.delete();

        removeRestaurantFromCategories(restaurant);
        removeRestaurantPhoto(restaurant);

        try {
            restaurantRepository.save(restaurant);
        } catch (Exception e) {
            throw new EntityDeletionException("restaurant", "id", id);
        }

        return true;
    }

    private void checkRestaurantCreationOrThrow(User user) {
        if (!restaurantRepository.existsByRestaurateur_Id(user.getId())) return;

        throw new RestaurateurAlreadyHasRestaurantException(user.getId());
    }

    private Address saveRestaurantAddress(Restaurant restaurant) {
        Address address = restaurant.getAddress();
        address.setRestaurant(restaurant);

        return addressService.save(address);
    }

    private List<Category> addRestaurantToCategories(Restaurant restaurant, List<Long> categoryIds) {
        List<Category> categories = new ArrayList<>();

        categoryIds.forEach(categoryId -> {
            Category category = categoryRepository
                    .findById(categoryId)
                    .orElse(null);
            Optional.ofNullable(category)
                    .map(c -> categoryService.addRestaurant(c.getId(), restaurant))
                    .ifPresent(categories::add);
        });

        return categories;
    }

    private String saveRestaurantPhoto(String restaurantPhotoBase64) {
        return Optional.ofNullable(restaurantPhotoBase64)
                .map(photoBase64 -> googleDriveService.uploadBase64Image(photoBase64, GoogleDriveFileType.RESTAURANT_PHOTO))
                .orElse(null);
    }

    private void removeRestaurantPhoto(Restaurant restaurant) {
        Optional.ofNullable(restaurant.getPhotoUrl())
                .ifPresent(googleDriveService::deleteImage);
    }

    private void sendRestaurantRegistrationEmail(Restaurant restaurant) {
        Map<EmailPlaceholder, Object> variables = Map.of(
                EmailPlaceholder.RESTAURANT_NAME, restaurant.getName(),
                EmailPlaceholder.RESTAURATEUR_NAME, restaurant.getRestaurateur().getName(),
                EmailPlaceholder.RESTAURATEUR_SURNAME, restaurant.getRestaurateur().getSurname()
        );
        emailService.sendTemplatedEmail(
                restaurant.getRestaurateur().getEmail(),
                EmailTemplateType.RESTAURANT_REGISTRATION,
                variables
        );
    }

    private List<Restaurant> getRestaurantsBasedOnUserRole(User user) {
        if (UserRoleUtils.isAdmin(user) || UserRoleUtils.isModerator(user)) {
            return restaurantRepository.findAll();
        } else {
            return restaurantRepository.findAllByApproved(true);
        }
    }

    private Restaurant getRestaurantByIdBasedOnUserRole(User user, long id) {
        if (UserRoleUtils.isAdmin(user) || UserRoleUtils.isModerator(user)) {
            return restaurantRepository
                    .findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("restaurant", "id", id));
        } else {
            return restaurantRepository
                    .findByIdAndApproved(id, true)
                    .orElseThrow(() -> new EntityNotFoundException("restaurant", "id", id));
        }
    }

    private List<Restaurant> getRestaurantByCategoryBasedOnUserRole(User user, long categoryId) {
        if (UserRoleUtils.isAdmin(user) || UserRoleUtils.isModerator(user)) {
            return restaurantRepository
                    .findAllByCategory_Id(categoryId);
        } else {
            return restaurantRepository
                    .findAllByCategory_IdAndApproved(categoryId, true);
        }
    }

    private void sendRestaurantApprovalEmail(Restaurant restaurant) {
        Map<EmailPlaceholder, Object> variables = Map.of(
                EmailPlaceholder.RESTAURANT_NAME, restaurant.getName(),
                EmailPlaceholder.RESTAURATEUR_NAME, restaurant.getRestaurateur().getName(),
                EmailPlaceholder.RESTAURATEUR_SURNAME, restaurant.getRestaurateur().getSurname()
        );
        emailService.sendTemplatedEmail(
                restaurant.getRestaurateur().getEmail(),
                EmailTemplateType.RESTAURANT_APPROVED,
                variables
        );
    }

    private void checkRestaurantAccessOrThrow(User user, Restaurant restaurant) {
        if (restaurant.getRestaurateur().getId() == user.getId()) return;

        throw new ForbiddenRestaurantAccessException();
    }

    private Address updateRestaurantAddress(Restaurant restaurant, RestaurantRequestDTO restaurantDTO) {
        Address newAddress = new Address(
                restaurant.getAddress().getId(),
                restaurantDTO.getCity(),
                restaurantDTO.getProvince(),
                restaurantDTO.getStreet(),
                restaurantDTO.getCivicNumber(),
                restaurantDTO.getPostalCode(),
                restaurant
        );
        return addressService.update(restaurant.getAddress().getId(), newAddress);
    }

    private List<Category> updateRestaurantCategories(Restaurant restaurant, List<Long> categoryIds) {
        restaurant.getCategories().forEach(category ->
                category.getRestaurants().remove(restaurant)
        );
        categoryRepository.saveAll(restaurant.getCategories());

        return addRestaurantToCategories(restaurant, categoryIds);
    }

    private String updateRestaurantPhoto(Restaurant restaurant, String photoBase64) {
        removeRestaurantPhoto(restaurant);
        return saveRestaurantPhoto(photoBase64);
    }

    private void removeRestaurantFromCategories(Restaurant restaurant) {
        restaurant.getCategories().forEach(category ->
                category.getRestaurants().remove(restaurant)
        );
        categoryRepository.saveAll(restaurant.getCategories());
    }
}