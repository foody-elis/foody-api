package com.example.foody.service;

import com.example.foody.dto.request.WeekDayInfoRequestDTO;
import com.example.foody.dto.request.WeekDayInfoUpdateRequestDTO;
import com.example.foody.dto.response.WeekDayInfoResponseDTO;

import java.util.List;

/**
 * Service interface for managing week day information.
 */
public interface WeekDayInfoService {

    /**
     * Saves a new week day information.
     *
     * @param weekDayInfoRequestDTO the week day information data transfer object containing details
     * @return the saved week day information response data transfer object
     */
    WeekDayInfoResponseDTO save(WeekDayInfoRequestDTO weekDayInfoRequestDTO);

    /**
     * Retrieves all week day information.
     *
     * @return a list of all week day information response data transfer objects
     */
    List<WeekDayInfoResponseDTO> findAll();

    /**
     * Finds all week day information by restaurant ID.
     *
     * @param restaurantId the ID of the restaurant
     * @return a list of week day information response data transfer objects for the specified restaurant
     */
    List<WeekDayInfoResponseDTO> findAllByRestaurant(long restaurantId);

    /**
     * Updates week day information by its ID.
     *
     * @param id the ID of the week day information to update
     * @param weekDayInfoUpdateRequestDTO the week day information data transfer object containing updated details
     * @return the updated week day information response data transfer object
     */
    WeekDayInfoResponseDTO update(long id, WeekDayInfoUpdateRequestDTO weekDayInfoUpdateRequestDTO);
}