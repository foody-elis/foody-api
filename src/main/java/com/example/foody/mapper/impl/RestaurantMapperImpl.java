package com.example.foody.mapper.impl;

import com.example.foody.builder.AddressBuilder;
import com.example.foody.builder.RestaurantBuilder;
import com.example.foody.dto.request.RestaurantRequestDTO;
import com.example.foody.dto.response.DetailedRestaurantResponseDTO;
import com.example.foody.dto.response.RestaurantResponseDTO;
import com.example.foody.helper.DishHelper;
import com.example.foody.mapper.CategoryMapper;
import com.example.foody.mapper.RestaurantMapper;
import com.example.foody.mapper.ReviewMapper;
import com.example.foody.mapper.SittingTimeMapper;
import com.example.foody.model.*;
import com.example.foody.model.user.RestaurateurUser;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Implementation of the {@link RestaurantMapper} interface.
 * <p>
 * Provides methods to convert between {@link Restaurant} entities and DTOs.
 */
@Component
@AllArgsConstructor
public class RestaurantMapperImpl implements RestaurantMapper {

    private final RestaurantBuilder restaurantBuilder;
    private final AddressBuilder addressBuilder;
    private final CategoryMapper categoryMapper;
    private final SittingTimeMapper sittingTimeMapper;
    private final ReviewMapper reviewMapper;
    private final DishHelper dishHelper;

    /**
     * {@inheritDoc}
     * <p>
     * Converts a {@link Restaurant} entity to a {@link RestaurantResponseDTO}.
     *
     * @param restaurant the Restaurant entity to convert
     * @return the converted RestaurantResponseDTO
     */
    @Override
    public RestaurantResponseDTO restaurantToRestaurantResponseDTO(Restaurant restaurant) {
        if (restaurant == null) {
            return null;
        }

        RestaurantResponseDTO restaurantResponseDTO = new RestaurantResponseDTO();
        mapCommonFields(restaurant, restaurantResponseDTO);

        return restaurantResponseDTO;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Converts a {@link Restaurant} entity to a {@link DetailedRestaurantResponseDTO}.
     *
     * @param restaurant    the Restaurant entity to convert
     * @param averageRating the average rating of the restaurant
     * @param sittingTimes  the list of sitting times for the restaurant
     * @param dishes        the list of dishes offered by the restaurant
     * @param reviews       the list of reviews for the restaurant
     * @return the converted DetailedRestaurantResponseDTO
     */
    @Override
    public DetailedRestaurantResponseDTO restaurantToDetailedRestaurantResponseDTO(
            Restaurant restaurant,
            double averageRating,
            List<SittingTime> sittingTimes,
            List<Dish> dishes,
            List<Review> reviews
    ) {
        if (restaurant == null) {
            return null;
        }

        DetailedRestaurantResponseDTO detailedRestaurantResponseDTO = new DetailedRestaurantResponseDTO();

        mapCommonFields(restaurant, detailedRestaurantResponseDTO);
        detailedRestaurantResponseDTO.setAverageRating(averageRating);
        detailedRestaurantResponseDTO.setSittingTimes(
                sittingTimeMapper.sittingTimesToSittingTimeResponseDTOs(sittingTimes)
        );
        detailedRestaurantResponseDTO.setDishes(
                dishHelper.buildDishResponseDTOs(dishes)
        );
        detailedRestaurantResponseDTO.setReviews(
                reviewMapper.reviewsToReviewResponseDTOs(reviews)
        );

        return detailedRestaurantResponseDTO;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Converts a {@link RestaurantRequestDTO} to a {@link Restaurant} entity.
     *
     * @param restaurantRequestDTO the RestaurantRequestDTO to convert
     * @return the converted Restaurant entity
     */
    @Override
    public Restaurant restaurantRequestDTOToRestaurant(RestaurantRequestDTO restaurantRequestDTO) {
        if (restaurantRequestDTO == null) {
            return null;
        }

        Address address = addressBuilder
                .city(restaurantRequestDTO.getCity())
                .province(restaurantRequestDTO.getProvince())
                .street(restaurantRequestDTO.getStreet())
                .civicNumber(restaurantRequestDTO.getCivicNumber())
                .postalCode(restaurantRequestDTO.getPostalCode())
                .build();

        return restaurantBuilder
                .name(restaurantRequestDTO.getName())
                .description(restaurantRequestDTO.getDescription())
                .phoneNumber(restaurantRequestDTO.getPhoneNumber())
                .seats(restaurantRequestDTO.getSeats())
                .address(address)
                .build();
    }

    /**
     * {@inheritDoc}
     * <p>
     * Updates a {@link Restaurant} entity from a {@link RestaurantRequestDTO}.
     *
     * @param restaurant           the Restaurant entity to update
     * @param restaurantRequestDTO the RestaurantRequestDTO with updated information
     */
    @Override
    public void updateRestaurantFromRestaurantRequestDTO(Restaurant restaurant, RestaurantRequestDTO restaurantRequestDTO) {
        if (restaurantRequestDTO == null || restaurant == null) {
            return;
        }

        restaurant.setName(restaurantRequestDTO.getName());
        restaurant.setDescription(restaurantRequestDTO.getDescription());
        restaurant.setPhoneNumber(restaurantRequestDTO.getPhoneNumber());
        restaurant.setSeats(restaurantRequestDTO.getSeats());

        Address address = restaurant.getAddress();
        if (address != null) {
            address.setCity(restaurantRequestDTO.getCity());
            address.setProvince(restaurantRequestDTO.getProvince());
            address.setStreet(restaurantRequestDTO.getStreet());
            address.setCivicNumber(restaurantRequestDTO.getCivicNumber());
            address.setPostalCode(restaurantRequestDTO.getPostalCode());
        }
    }

    /**
     * Maps common fields from a {@link Restaurant} entity to a {@link RestaurantResponseDTO}.
     *
     * @param restaurant            the Restaurant entity
     * @param restaurantResponseDTO the RestaurantResponseDTO to populate
     */
    private void mapCommonFields(Restaurant restaurant, RestaurantResponseDTO restaurantResponseDTO) {
        restaurantResponseDTO.setId(restaurant.getId());
        restaurantResponseDTO.setName(restaurant.getName());
        restaurantResponseDTO.setDescription(restaurant.getDescription());
        restaurantResponseDTO.setPhotoUrl(restaurant.getPhotoUrl());
        restaurantResponseDTO.setPhoneNumber(restaurant.getPhoneNumber());
        restaurantResponseDTO.setSeats(restaurant.getSeats());
        restaurantResponseDTO.setApproved(restaurant.isApproved());
        restaurantResponseDTO.setCategories(
                categoryMapper.categoriesToCategoryResponseDTOs(restaurant.getCategories())
        );
        restaurantResponseDTO.setRestaurateurId(restaurantRestaurateurId(restaurant));
        restaurantResponseDTO.setRestaurateurEmail(restaurantRestaurateurEmail(restaurant));
        restaurantResponseDTO.setCity(restaurantAddressCity(restaurant));
        restaurantResponseDTO.setProvince(restaurantAddressProvince(restaurant));
        restaurantResponseDTO.setStreet(restaurantAddressStreet(restaurant));
        restaurantResponseDTO.setCivicNumber(restaurantAddressCivicNumber(restaurant));
        restaurantResponseDTO.setPostalCode(restaurantAddressPostalCode(restaurant));
    }

    /**
     * Retrieves the restaurateur ID from a {@link Restaurant} entity.
     *
     * @param restaurant the Restaurant entity
     * @return the restaurateur ID, or null if not available
     */
    private Long restaurantRestaurateurId(Restaurant restaurant) {
        if (restaurant == null) {
            return null;
        }
        RestaurateurUser restaurateur = restaurant.getRestaurateur();
        if (restaurateur == null) {
            return null;
        }
        return restaurateur.getId();
    }

    /**
     * Retrieves the restaurateur email from a {@link Restaurant} entity.
     *
     * @param restaurant the Restaurant entity
     * @return the restaurateur email, or null if not available
     */
    private String restaurantRestaurateurEmail(Restaurant restaurant) {
        if (restaurant == null) {
            return null;
        }
        RestaurateurUser restaurateur = restaurant.getRestaurateur();
        if (restaurateur == null) {
            return null;
        }
        return restaurateur.getEmail();
    }

    /**
     * Retrieves the city from the address of a {@link Restaurant} entity.
     *
     * @param restaurant the Restaurant entity
     * @return the city, or null if not available
     */
    private String restaurantAddressCity(Restaurant restaurant) {
        if (restaurant == null) {
            return null;
        }
        Address address = restaurant.getAddress();
        if (address == null) {
            return null;
        }
        return address.getCity();
    }

    /**
     * Retrieves the province from the address of a {@link Restaurant} entity.
     *
     * @param restaurant the Restaurant entity
     * @return the province, or null if not available
     */
    private String restaurantAddressProvince(Restaurant restaurant) {
        if (restaurant == null) {
            return null;
        }
        Address address = restaurant.getAddress();
        if (address == null) {
            return null;
        }
        return address.getProvince();
    }

    /**
     * Retrieves the street from the address of a {@link Restaurant} entity.
     *
     * @param restaurant the Restaurant entity
     * @return the street, or null if not available
     */
    private String restaurantAddressStreet(Restaurant restaurant) {
        if (restaurant == null) {
            return null;
        }
        Address address = restaurant.getAddress();
        if (address == null) {
            return null;
        }
        return address.getStreet();
    }

    /**
     * Retrieves the civic number from the address of a {@link Restaurant} entity.
     *
     * @param restaurant the Restaurant entity
     * @return the civic number, or null if not available
     */
    private String restaurantAddressCivicNumber(Restaurant restaurant) {
        if (restaurant == null) {
            return null;
        }
        Address address = restaurant.getAddress();
        if (address == null) {
            return null;
        }
        return address.getCivicNumber();
    }

    /**
     * Retrieves the postal code from the address of a {@link Restaurant} entity.
     *
     * @param restaurant the Restaurant entity
     * @return the postal code, or null if not available
     */
    private String restaurantAddressPostalCode(Restaurant restaurant) {
        if (restaurant == null) {
            return null;
        }
        Address address = restaurant.getAddress();
        if (address == null) {
            return null;
        }
        return address.getPostalCode();
    }
}