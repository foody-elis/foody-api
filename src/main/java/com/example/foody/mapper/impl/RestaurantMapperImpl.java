package com.example.foody.mapper.impl;

import com.example.foody.builder.AddressBuilder;
import com.example.foody.builder.RestaurantBuilder;
import com.example.foody.dto.request.RestaurantRequestDTO;
import com.example.foody.dto.response.RestaurantResponseDTO;
import com.example.foody.mapper.CategoryMapper;
import com.example.foody.mapper.RestaurantMapper;
import com.example.foody.model.Address;
import com.example.foody.model.Restaurant;
import com.example.foody.model.user.RestaurateurUser;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class RestaurantMapperImpl implements RestaurantMapper {
    private final RestaurantBuilder restaurantBuilder;
    private final AddressBuilder addressBuilder;
    private final CategoryMapper categoryMapper;

    public RestaurantMapperImpl(RestaurantBuilder restaurantBuilder, AddressBuilder addressBuilder, CategoryMapper categoryMapper) {
        this.restaurantBuilder = restaurantBuilder;
        this.addressBuilder = addressBuilder;
        this.categoryMapper = categoryMapper;
    }

    @Override
    public RestaurantResponseDTO restaurantToRestaurantResponseDTO(Restaurant restaurant) {
        if (restaurant == null) {
            return null;
        }

        RestaurantResponseDTO restaurantResponseDTO = new RestaurantResponseDTO();

        restaurantResponseDTO.setCategories(
                categoryMapper.categoriesToCategoryResponseDTOs(restaurant.getCategories())
        );
        restaurantResponseDTO.setRestaurateurId(restaurantRestaurateurId(restaurant));
        restaurantResponseDTO.setCity(restaurantAddressCity(restaurant));
        restaurantResponseDTO.setProvince(restaurantAddressProvince(restaurant));
        restaurantResponseDTO.setStreet(restaurantAddressStreet(restaurant));
        restaurantResponseDTO.setCivicNumber(restaurantAddressCivicNumber(restaurant));
        restaurantResponseDTO.setPostalCode(restaurantAddressPostalCode(restaurant));
        restaurantResponseDTO.setId(restaurant.getId());
        restaurantResponseDTO.setName(restaurant.getName());
        restaurantResponseDTO.setDescription(restaurant.getDescription());
        restaurantResponseDTO.setPhoneNumber(restaurant.getPhoneNumber());
        restaurantResponseDTO.setSeats(restaurant.getSeats());
        restaurantResponseDTO.setApproved(restaurant.isApproved());

        return restaurantResponseDTO;
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
                .address(address) // I set the address
                .build();
    }

    @Override
    public List<RestaurantResponseDTO> restaurantsToRestaurantResponseDTOs(List<Restaurant> restaurants) {
        if (restaurants == null) {
            return null;
        }

        List<RestaurantResponseDTO> list = new ArrayList<>(restaurants.size());
        for (Restaurant restaurant : restaurants) {
            list.add(restaurantToRestaurantResponseDTO(restaurant));
        }

        return list;
    }

    private long restaurantRestaurateurId(Restaurant restaurant) {
        if (restaurant == null) {
            return 0L;
        }
        RestaurateurUser restaurateur = restaurant.getRestaurateur();
        if (restaurateur == null) {
            return 0L;
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
