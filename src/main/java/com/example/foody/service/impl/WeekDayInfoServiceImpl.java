package com.example.foody.service.impl;

import com.example.foody.dto.request.WeekDayInfoRequestDTO;
import com.example.foody.dto.response.WeekDayInfoResponseDTO;
import com.example.foody.exceptions.entity.EntityCreationException;
import com.example.foody.exceptions.entity.EntityDataIntegrityViolationException;
import com.example.foody.exceptions.entity.EntityDuplicateException;
import com.example.foody.exceptions.entity.EntityNotFoundException;
import com.example.foody.mapper.WeekDayInfoMapper;
import com.example.foody.model.Restaurant;
import com.example.foody.model.WeekDayInfo;
import com.example.foody.model.user.RestaurateurUser;
import com.example.foody.repository.RestaurantRepository;
import com.example.foody.repository.WeekDayInfoRepository;
import com.example.foody.service.SittingTimeService;
import com.example.foody.service.WeekDayInfoService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class WeekDayInfoServiceImpl implements WeekDayInfoService {
    private final WeekDayInfoRepository weekDayInfoRepository;
    private final RestaurantRepository restaurantRepository;
    private final SittingTimeService sittingTimeService;
    private final WeekDayInfoMapper weekDayInfoMapper;

    public WeekDayInfoServiceImpl(WeekDayInfoRepository weekDayInfoRepository, RestaurantRepository restaurantRepository, SittingTimeService sittingTimeService, WeekDayInfoMapper weekDayInfoMapper) {
        this.weekDayInfoRepository = weekDayInfoRepository;
        this.restaurantRepository = restaurantRepository;
        this.sittingTimeService = sittingTimeService;
        this.weekDayInfoMapper = weekDayInfoMapper;
    }

    @Override
    public WeekDayInfoResponseDTO save(WeekDayInfoRequestDTO weekDayInfoRequestDTO) {
        RestaurateurUser principal = (RestaurateurUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        WeekDayInfo weekDayInfo = weekDayInfoMapper.weekDayInfoRequestDTOToWeekDayInfo(weekDayInfoRequestDTO);
        Restaurant restaurant = getRestaurant(principal);

        weekDayInfo.setRestaurant(restaurant);

        checkWeekDayInfoCreationOrThrow(weekDayInfo, restaurant);

        try {
            weekDayInfo = weekDayInfoRepository.save(weekDayInfo);
        } catch (DataIntegrityViolationException e) {
            // todo specify DataIntegrityViolationException's violated constraint in EntityDataIntegrityViolationException
            throw new EntityDataIntegrityViolationException("week day info");
        } catch (Exception e) {
            throw new EntityCreationException("week day info");
        }

        sittingTimeService.createForWeekDayInfo(weekDayInfo);

        return weekDayInfoMapper.weekDayInfoToWeekDayInfoResponseDTO(weekDayInfo);
    }

    @Override
    public List<WeekDayInfoResponseDTO> findAll() {
        List<WeekDayInfo> weekDayInfos = weekDayInfoRepository.findAllByDeletedAtIsNull();
        return weekDayInfoMapper.weekDayInfosToWeekDayInfoResponseDTOs(weekDayInfos);
    }

    @Override
    public boolean remove(long id) {
        WeekDayInfo weekDayInfo = weekDayInfoRepository
                .findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new EntityNotFoundException("week day info", "id", id));
        weekDayInfo.setDeletedAt(LocalDateTime.now());

        removeAssociatedEntities(weekDayInfo);

        try {
            weekDayInfoRepository.save(weekDayInfo);
        } catch (Exception e) {
            throw new EntityNotFoundException("week day info", "id", id);
        }

        return true;
    }

    private Restaurant getRestaurant(RestaurateurUser restaurateur) {
        if (restaurateur.getRestaurant() == null) {
            throw new EntityNotFoundException("restaurant", "restaurateur_id", restaurateur.getId());
        } else {
            return restaurantRepository
                    .findByIdAndDeletedAtIsNull(restaurateur.getRestaurant().getId())
                    .orElseThrow(() -> new EntityNotFoundException("restaurant", "id", restaurateur.getRestaurant().getId()));
        }
    }

    private void checkWeekDayInfoCreationOrThrow(WeekDayInfo weekDayInfo, Restaurant restaurant) {
        checkWeekDayInfoExists(weekDayInfo, restaurant);
    }

    private void checkWeekDayInfoExists(WeekDayInfo weekDayInfo, Restaurant restaurant) {
        if (!weekDayInfoRepository.existsByDeletedAtIsNullAndWeekDayAndRestaurantId(weekDayInfo.getWeekDay(), restaurant.getId())) return;

        throw new EntityDuplicateException("week day info", "weekDay", weekDayInfo.getWeekDay(), "restaurantId", restaurant.getId());
    }

    private void removeAssociatedEntities(WeekDayInfo weekDayInfo) {
        removeSittingTimes(weekDayInfo);
    }

    private void removeSittingTimes(WeekDayInfo weekDayInfo) {
        weekDayInfo.getSittingTimes().forEach(sittingTime ->
                sittingTimeService.remove(sittingTime.getId())
        );
    }
}