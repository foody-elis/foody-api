package com.example.foody.mapper;

import com.example.foody.dto.request.RestaurantRequestDTO;
import com.example.foody.dto.response.RestaurantResponseDTO;
import com.example.foody.model.Address;
import com.example.foody.model.Restaurant;
import com.example.foody.model.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class RestaurantMapperImpl implements RestaurantMapper {

    @Override
    public RestaurantResponseDTO restaurantToRestaurantResponseDTO(Restaurant restaurant) {
        if ( restaurant == null ) {
            return null;
        }

        RestaurantResponseDTO restaurantResponseDTO = new RestaurantResponseDTO();

        restaurantResponseDTO.setUserId( restaurantUserId( restaurant ) );
        restaurantResponseDTO.setCity( restaurantAddressCity( restaurant ) );
        restaurantResponseDTO.setProvince( restaurantAddressProvince( restaurant ) );
        restaurantResponseDTO.setStreet( restaurantAddressStreet( restaurant ) );
        restaurantResponseDTO.setCivicNumber( restaurantAddressCivicNumber( restaurant ) );
        restaurantResponseDTO.setPostalCode( restaurantAddressPostalCode( restaurant ) );
        restaurantResponseDTO.setId( restaurant.getId() );
        restaurantResponseDTO.setName( restaurant.getName() );
        restaurantResponseDTO.setDescription( restaurant.getDescription() );
        restaurantResponseDTO.setPhoneNumber( restaurant.getPhoneNumber() );
        restaurantResponseDTO.setSeats( restaurant.getSeats() );

        return restaurantResponseDTO;
    }

    @Override
    public Restaurant restaurantRequestDTOToRestaurant(RestaurantRequestDTO restaurantRequestDTO) {
        if ( restaurantRequestDTO == null ) {
            return null;
        }

        Restaurant restaurant = new Restaurant();

        restaurant.setName( restaurantRequestDTO.getName() );
        restaurant.setDescription( restaurantRequestDTO.getDescription() );
        restaurant.setPhoneNumber( restaurantRequestDTO.getPhoneNumber() );
        restaurant.setSeats( restaurantRequestDTO.getSeats() );

        // I add the address to the restaurant
        Address address = new Address();

        address.setCity( restaurantRequestDTO.getCity() );
        address.setProvince( restaurantRequestDTO.getProvince() );
        address.setStreet( restaurantRequestDTO.getStreet() );
        address.setCivicNumber( restaurantRequestDTO.getCivicNumber() );
        address.setPostalCode( restaurantRequestDTO.getPostalCode() );

        restaurant.setAddress( address );

        return restaurant;
    }

    @Override
    public List<RestaurantResponseDTO> restaurantsToRestaurantResponseDTOs(List<Restaurant> restaurants) {
        if ( restaurants == null ) {
            return null;
        }

        List<RestaurantResponseDTO> list = new ArrayList<RestaurantResponseDTO>( restaurants.size() );
        for ( Restaurant restaurant : restaurants ) {
            list.add( restaurantToRestaurantResponseDTO( restaurant ) );
        }

        return list;
    }

    private long restaurantUserId(Restaurant restaurant) {
        if ( restaurant == null ) {
            return 0L;
        }
        User user = restaurant.getUser();
        if ( user == null ) {
            return 0L;
        }
        long id = user.getId();
        return id;
    }

    private String restaurantAddressCity(Restaurant restaurant) {
        if ( restaurant == null ) {
            return null;
        }
        Address address = restaurant.getAddress();
        if ( address == null ) {
            return null;
        }
        String city = address.getCity();
        if ( city == null ) {
            return null;
        }
        return city;
    }

    private String restaurantAddressProvince(Restaurant restaurant) {
        if ( restaurant == null ) {
            return null;
        }
        Address address = restaurant.getAddress();
        if ( address == null ) {
            return null;
        }
        String province = address.getProvince();
        if ( province == null ) {
            return null;
        }
        return province;
    }

    private String restaurantAddressStreet(Restaurant restaurant) {
        if ( restaurant == null ) {
            return null;
        }
        Address address = restaurant.getAddress();
        if ( address == null ) {
            return null;
        }
        String street = address.getStreet();
        if ( street == null ) {
            return null;
        }
        return street;
    }

    private String restaurantAddressCivicNumber(Restaurant restaurant) {
        if ( restaurant == null ) {
            return null;
        }
        Address address = restaurant.getAddress();
        if ( address == null ) {
            return null;
        }
        String civicNumber = address.getCivicNumber();
        if ( civicNumber == null ) {
            return null;
        }
        return civicNumber;
    }

    private String restaurantAddressPostalCode(Restaurant restaurant) {
        if ( restaurant == null ) {
            return null;
        }
        Address address = restaurant.getAddress();
        if ( address == null ) {
            return null;
        }
        String postalCode = address.getPostalCode();
        if ( postalCode == null ) {
            return null;
        }
        return postalCode;
    }
}
