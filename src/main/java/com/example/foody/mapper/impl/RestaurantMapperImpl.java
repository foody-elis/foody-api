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
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RestaurantMapperImpl implements RestaurantMapper {
    private final RestaurantBuilder restaurantBuilder;
    private final AddressBuilder addressBuilder;
    private final CategoryMapper categoryMapper;
    private final SittingTimeMapper sittingTimeMapper;
    private final ReviewMapper reviewMapper;
    private final DishHelper dishHelper;

    public RestaurantMapperImpl(RestaurantBuilder restaurantBuilder, AddressBuilder addressBuilder, CategoryMapper categoryMapper, SittingTimeMapper sittingTimeMapper, ReviewMapper reviewMapper, DishHelper dishHelper) {
        this.restaurantBuilder = restaurantBuilder;
        this.addressBuilder = addressBuilder;
        this.categoryMapper = categoryMapper;
        this.sittingTimeMapper = sittingTimeMapper;
        this.reviewMapper = reviewMapper;
        this.dishHelper = dishHelper;
    }

    @Override
    public RestaurantResponseDTO restaurantToRestaurantResponseDTO(Restaurant restaurant) {
        if (restaurant == null) {
            return null;
        }

        RestaurantResponseDTO restaurantResponseDTO = new RestaurantResponseDTO();
        mapCommonFields(restaurant, restaurantResponseDTO);

        return restaurantResponseDTO;
    }

    @Override
    public DetailedRestaurantResponseDTO restaurantToDetailedRestaurantResponseDTO(Restaurant restaurant, double averageRating, List<SittingTime> sittingTimes, List<Dish> dishes, List<Review> reviews) {
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
        restaurantResponseDTO.setCity(restaurantAddressCity(restaurant));
        restaurantResponseDTO.setProvince(restaurantAddressProvince(restaurant));
        restaurantResponseDTO.setStreet(restaurantAddressStreet(restaurant));
        restaurantResponseDTO.setCivicNumber(restaurantAddressCivicNumber(restaurant));
        restaurantResponseDTO.setPostalCode(restaurantAddressPostalCode(restaurant));
    }

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