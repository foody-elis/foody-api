package com.example.foody.helper;

import com.example.foody.dto.response.DetailedRestaurantResponseDTO;
import com.example.foody.model.Restaurant;

import java.util.List;

/**
 * Helper interface for building {@link DetailedRestaurantResponseDTO} objects.
 */
public interface RestaurantHelper {

    /**
     * Builds a {@link DetailedRestaurantResponseDTO} from a given {@link Restaurant}.
     *
     * @param restaurant the Restaurant object to convert
     * @return the constructed {@link DetailedRestaurantResponseDTO}
     */
    DetailedRestaurantResponseDTO buildDetailedRestaurantResponseDTO(Restaurant restaurant);

    /**
     * Builds a list of {@link DetailedRestaurantResponseDTO} objects from a given list of {@link Restaurant} objects.
     *
     * @param restaurants the list of Restaurant objects to convert
     * @return the list of constructed {@link DetailedRestaurantResponseDTO} objects
     */
    List<DetailedRestaurantResponseDTO> buildDetailedRestaurantResponseDTOs(List<Restaurant> restaurants);
}