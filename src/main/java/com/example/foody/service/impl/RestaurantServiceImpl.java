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
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Implementation of the RestaurantService interface.
 * <p>
 * Provides methods to create, update, and delete {@link Restaurant} objects.
 */
@Service
@Transactional(rollbackOn = Exception.class)
@AllArgsConstructor
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final CategoryRepository categoryRepository;
    private final RestaurantMapper restaurantMapper;
    private final RestaurantHelper restaurantHelper;
    private final CategoryService categoryService;
    private final AddressService addressService;
    private final GoogleDriveService googleDriveService;
    private final EmailService emailService;

    /**
     * {@inheritDoc}
     * <p>
     * This method persists a new {@link Restaurant} entity to the database.
     *
     * @param restaurantDTO the restaurant data transfer object
     * @return the saved restaurant response data transfer object
     * @throws EntityCreationException if there is an error during restaurant creation
     */
    @Override
    public RestaurantResponseDTO save(RestaurantRequestDTO restaurantDTO) {
        RestaurateurUser principal =
                (RestaurateurUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
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

    /**
     * {@inheritDoc}
     * <p>
     * This method retrieves all {@link Restaurant} entities from the database.
     *
     * @return a list of detailed restaurant response data transfer objects
     */
    @Override
    public List<DetailedRestaurantResponseDTO> findAll() {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Restaurant> restaurants = getRestaurantsBasedOnUserRole(principal);
        return restaurantHelper.buildDetailedRestaurantResponseDTOs(restaurants);
    }

    /**
     * {@inheritDoc}
     * <p>
     * This method retrieves a {@link Restaurant} by its ID.
     *
     * @param id the restaurant ID
     * @return the detailed restaurant response data transfer object
     * @throws EntityNotFoundException if the restaurant is not found
     */
    @Override
    public DetailedRestaurantResponseDTO findById(long id) {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Restaurant restaurant = getRestaurantByIdBasedOnUserRole(principal, id);
        return restaurantHelper.buildDetailedRestaurantResponseDTO(restaurant);
    }

    /**
     * {@inheritDoc}
     * <p>
     * This method retrieves a {@link Restaurant} by its restaurateur's ID.
     *
     * @param restaurateurId the restaurateur's ID
     * @return the detailed restaurant response data transfer object
     * @throws EntityNotFoundException if the restaurant is not found
     */
    @Override
    public DetailedRestaurantResponseDTO findByRestaurateur(long restaurateurId) {
        Restaurant restaurant = restaurantRepository
                .findByRestaurateur_Id(restaurateurId)
                .orElseThrow(() -> new EntityNotFoundException("restaurant", "restaurateurId", restaurateurId));
        return restaurantHelper.buildDetailedRestaurantResponseDTO(restaurant);
    }

    /**
     * {@inheritDoc}
     * <p>
     * This method retrieves all {@link Restaurant} entities for a specific category.
     *
     * @param categoryId the category ID
     * @return a list of detailed restaurant response data transfer objects
     */
    @Override
    public List<DetailedRestaurantResponseDTO> findAllByCategory(long categoryId) {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Restaurant> restaurants = getRestaurantByCategoryBasedOnUserRole(principal, categoryId);
        return restaurantHelper.buildDetailedRestaurantResponseDTOs(restaurants);
    }

    /**
     * {@inheritDoc}
     * <p>
     * This method approves a {@link Restaurant} by its ID.
     *
     * @param id the restaurant ID
     * @return the detailed restaurant response data transfer object
     * @throws EntityNotFoundException if the restaurant is not found
     * @throws EntityEditException     if there is an error during restaurant approval
     */
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

    /**
     * {@inheritDoc}
     * <p>
     * This method updates a {@link Restaurant} by its ID.
     *
     * @param id            the restaurant ID
     * @param restaurantDTO the restaurant data transfer object
     * @return the updated detailed restaurant response data transfer object
     * @throws EntityNotFoundException if the restaurant is not found
     * @throws EntityEditException     if there is an error during restaurant update
     */
    @Override
    public DetailedRestaurantResponseDTO update(long id, RestaurantRequestDTO restaurantDTO) {
        RestaurateurUser principal =
                (RestaurateurUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
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

    /**
     * {@inheritDoc}
     * <p>
     * This method removes a {@link Restaurant} by its ID.
     *
     * @param id the restaurant ID
     * @return true if the restaurant was successfully removed, false otherwise
     * @throws EntityNotFoundException if the restaurant is not found
     * @throws EntityDeletionException if there is an error during restaurant deletion
     */
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

    /**
     * Checks if the user can create a restaurant.
     *
     * @param user the user to check
     * @throws RestaurateurAlreadyHasRestaurantException if the user already has a restaurant
     */
    private void checkRestaurantCreationOrThrow(User user) {
        if (!restaurantRepository.existsByRestaurateur_Id(user.getId())) return;

        throw new RestaurateurAlreadyHasRestaurantException(user.getId());
    }

    /**
     * Saves the address of a restaurant.
     *
     * @param restaurant the restaurant
     * @return the saved address
     */
    private Address saveRestaurantAddress(Restaurant restaurant) {
        Address address = restaurant.getAddress();
        address.setRestaurant(restaurant);

        return addressService.save(address);
    }

    /**
     * Adds a restaurant to categories.
     *
     * @param restaurant  the restaurant
     * @param categoryIds the list of category IDs
     * @return the list of categories
     */
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

    /**
     * Saves the photo of a restaurant.
     *
     * @param restaurantPhotoBase64 the base64 encoded photo
     * @return the URL of the saved photo
     */
    private String saveRestaurantPhoto(String restaurantPhotoBase64) {
        return Optional.ofNullable(restaurantPhotoBase64)
                .map(photoBase64 -> googleDriveService.uploadBase64Image(
                        photoBase64,
                        GoogleDriveFileType.RESTAURANT_PHOTO
                ))
                .orElse(null);
    }

    /**
     * Removes the photo of a restaurant.
     *
     * @param restaurant the restaurant
     */
    private void removeRestaurantPhoto(Restaurant restaurant) {
        Optional.ofNullable(restaurant.getPhotoUrl())
                .ifPresent(googleDriveService::deleteImage);
    }

    /**
     * Sends a restaurant registration email.
     *
     * @param restaurant the restaurant
     */
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

    /**
     * Retrieves restaurants based on the user's role.
     *
     * @param user the user
     * @return the list of restaurants
     */
    private List<Restaurant> getRestaurantsBasedOnUserRole(User user) {
        if (UserRoleUtils.isAdmin(user) || UserRoleUtils.isModerator(user)) {
            return restaurantRepository.findAll();
        } else {
            return restaurantRepository.findAllByApproved(true);
        }
    }

    /**
     * Retrieves a restaurant by its ID based on the user's role.
     *
     * @param user the user
     * @param id   the restaurant ID
     * @return the restaurant
     * @throws EntityNotFoundException if the restaurant is not found
     */
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

    /**
     * Retrieves restaurants by category ID based on the user's role.
     *
     * @param user       the user
     * @param categoryId the category ID
     * @return the list of restaurants
     */
    private List<Restaurant> getRestaurantByCategoryBasedOnUserRole(User user, long categoryId) {
        if (UserRoleUtils.isAdmin(user) || UserRoleUtils.isModerator(user)) {
            return restaurantRepository
                    .findAllByCategories_Id(categoryId);
        } else {
            return restaurantRepository
                    .findAllByCategories_IdAndApproved(categoryId, true);
        }
    }

    /**
     * Sends a restaurant approval email.
     *
     * @param restaurant the restaurant
     */
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

    /**
     * Checks if the user has access to the restaurant.
     *
     * @param user       the user
     * @param restaurant the restaurant
     * @throws ForbiddenRestaurantAccessException if the user does not have access to the restaurant
     */
    private void checkRestaurantAccessOrThrow(User user, Restaurant restaurant) {
        if (restaurant.getRestaurateur().getId() == user.getId()) return;

        throw new ForbiddenRestaurantAccessException();
    }

    /**
     * Updates the address of a restaurant.
     *
     * @param restaurant    the restaurant
     * @param restaurantDTO the restaurant data transfer object
     * @return the updated address
     */
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

    /**
     * Updates the categories of a restaurant.
     *
     * @param restaurant  the restaurant
     * @param categoryIds the list of category IDs
     * @return the list of updated categories
     */
    private List<Category> updateRestaurantCategories(Restaurant restaurant, List<Long> categoryIds) {
        restaurant.getCategories().forEach(category ->
                category.getRestaurants().remove(restaurant)
        );
        categoryRepository.saveAll(restaurant.getCategories());

        return addRestaurantToCategories(restaurant, categoryIds);
    }

    /**
     * Updates the photo of a restaurant.
     *
     * @param restaurant  the restaurant
     * @param photoBase64 the base64 encoded photo
     * @return the URL of the updated photo
     */
    private String updateRestaurantPhoto(Restaurant restaurant, String photoBase64) {
        removeRestaurantPhoto(restaurant);
        return saveRestaurantPhoto(photoBase64);
    }

    /**
     * Removes a restaurant from categories.
     *
     * @param restaurant the restaurant
     */
    private void removeRestaurantFromCategories(Restaurant restaurant) {
        restaurant.getCategories().forEach(category ->
                category.getRestaurants().remove(restaurant)
        );
        categoryRepository.saveAll(restaurant.getCategories());
    }
}