package com.example.foody.service.impl;

import com.example.foody.dto.request.WeekDayInfoRequestDTO;
import com.example.foody.dto.request.WeekDayInfoUpdateRequestDTO;
import com.example.foody.dto.response.WeekDayInfoResponseDTO;
import com.example.foody.exceptions.entity.EntityCreationException;
import com.example.foody.exceptions.entity.EntityDuplicateException;
import com.example.foody.exceptions.entity.EntityEditException;
import com.example.foody.exceptions.entity.EntityNotFoundException;
import com.example.foody.exceptions.restaurant.ForbiddenRestaurantAccessException;
import com.example.foody.mapper.WeekDayInfoMapper;
import com.example.foody.model.Restaurant;
import com.example.foody.model.WeekDayInfo;
import com.example.foody.model.user.RestaurateurUser;
import com.example.foody.model.user.User;
import com.example.foody.repository.RestaurantRepository;
import com.example.foody.repository.WeekDayInfoRepository;
import com.example.foody.service.SittingTimeService;
import com.example.foody.service.WeekDayInfoService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation of the WeekDayInfoService interface.
 * <p>
 * Provides methods to create, update, and retrieve {@link WeekDayInfo} objects.
 */
@Service
@Transactional(rollbackOn = Exception.class)
@AllArgsConstructor
public class WeekDayInfoServiceImpl implements WeekDayInfoService {

    private final WeekDayInfoRepository weekDayInfoRepository;
    private final RestaurantRepository restaurantRepository;
    private final WeekDayInfoMapper weekDayInfoMapper;
    private final SittingTimeService sittingTimeService;

    /**
     * {@inheritDoc}
     * <p>
     * This method persists a new {@link WeekDayInfo} entity to the database.
     *
     * @param weekDayInfoRequestDTO the week day information request data transfer object
     * @return the saved week day information response data transfer object
     * @throws EntityNotFoundException  if the restaurant is not found
     * @throws EntityDuplicateException if the week day information already exists for the restaurant
     * @throws EntityCreationException  if there is an error during week day information creation
     */
    @Override
    public WeekDayInfoResponseDTO save(WeekDayInfoRequestDTO weekDayInfoRequestDTO) {
        RestaurateurUser principal =
                (RestaurateurUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        WeekDayInfo weekDayInfo = weekDayInfoMapper.weekDayInfoRequestDTOToWeekDayInfo(weekDayInfoRequestDTO);
        Restaurant restaurant = restaurantRepository
                .findById(weekDayInfoRequestDTO.getRestaurantId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "restaurant",
                        "id",
                        weekDayInfoRequestDTO.getRestaurantId()
                ));

        weekDayInfo.setRestaurant(restaurant);

        checkRestaurantAccessOrThrow(principal, weekDayInfo.getRestaurant());

        try {
            weekDayInfo = weekDayInfoRepository.save(weekDayInfo);
        } catch (DataIntegrityViolationException e) {
            throw new EntityDuplicateException(
                    "week day info",
                    "weekDay",
                    weekDayInfo.getWeekDay(),
                    "restaurantId",
                    weekDayInfo.getRestaurant().getId()
            );
        } catch (Exception e) {
            throw new EntityCreationException("week day info");
        }

        sittingTimeService.createForWeekDayInfo(weekDayInfo);

        return weekDayInfoMapper.weekDayInfoToWeekDayInfoResponseDTO(weekDayInfo);
    }

    /**
     * {@inheritDoc}
     * <p>
     * This method retrieves all {@link WeekDayInfo} entities from the database.
     *
     * @return a list of week day information response data transfer objects
     */
    @Override
    public List<WeekDayInfoResponseDTO> findAll() {
        List<WeekDayInfo> weekDayInfos = weekDayInfoRepository.findAll();
        return weekDayInfoMapper.weekDayInfosToWeekDayInfoResponseDTOs(weekDayInfos);
    }

    /**
     * {@inheritDoc}
     * <p>
     * This method retrieves all {@link WeekDayInfo} entities from the database by restaurant ID.
     *
     * @param restaurantId the restaurant ID
     * @return a list of week day information response data transfer objects
     */
    @Override
    public List<WeekDayInfoResponseDTO> findAllByRestaurant(long restaurantId) {
        List<WeekDayInfo> weekDayInfos = weekDayInfoRepository.findAllByRestaurantIdOrderByWeekDay(restaurantId);
        return weekDayInfoMapper.weekDayInfosToWeekDayInfoResponseDTOs(weekDayInfos);
    }

    /**
     * {@inheritDoc}
     * <p>
     * This method updates a {@link WeekDayInfo} entity by its ID.
     *
     * @param id                          the week day information ID
     * @param weekDayInfoUpdateRequestDTO the week day information update request data transfer object
     * @return the updated week day information response data transfer object
     * @throws EntityNotFoundException if the week day information is not found
     * @throws EntityEditException     if there is an error during week day information update
     */
    @Override
    public WeekDayInfoResponseDTO update(long id, WeekDayInfoUpdateRequestDTO weekDayInfoUpdateRequestDTO) {
        RestaurateurUser principal =
                (RestaurateurUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        WeekDayInfo weekDayInfo = weekDayInfoRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("week day info", "id", id));

        checkRestaurantAccessOrThrow(principal, weekDayInfo.getRestaurant());

        weekDayInfoMapper.updateWeekDayInfoFromWeekDayInfoUpdateRequestDTO(weekDayInfo, weekDayInfoUpdateRequestDTO);

        try {
            weekDayInfo = weekDayInfoRepository.save(weekDayInfo);
        } catch (Exception e) {
            throw new EntityEditException("week day info", "id", id);
        }

        updateWeekDayInfoSittingTimes(weekDayInfo);

        return weekDayInfoMapper.weekDayInfoToWeekDayInfoResponseDTO(weekDayInfo);
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
     * Updates the sitting times for a week day information.
     *
     * @param weekDayInfo the week day information
     */
    private void updateWeekDayInfoSittingTimes(WeekDayInfo weekDayInfo) {
        weekDayInfo.getSittingTimes().forEach(sittingTime ->
                sittingTimeService.remove(sittingTime.getId())
        );
        sittingTimeService.createForWeekDayInfo(weekDayInfo);
    }
}