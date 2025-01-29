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
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WeekDayInfoServiceImpl implements WeekDayInfoService {
    private final WeekDayInfoRepository weekDayInfoRepository;
    private final RestaurantRepository restaurantRepository;
    private final WeekDayInfoMapper weekDayInfoMapper;
    private final SittingTimeService sittingTimeService;

    public WeekDayInfoServiceImpl(WeekDayInfoRepository weekDayInfoRepository, RestaurantRepository restaurantRepository, WeekDayInfoMapper weekDayInfoMapper, SittingTimeService sittingTimeService) {
        this.weekDayInfoRepository = weekDayInfoRepository;
        this.restaurantRepository = restaurantRepository;
        this.weekDayInfoMapper = weekDayInfoMapper;
        this.sittingTimeService = sittingTimeService;
    }

    @Override
    public WeekDayInfoResponseDTO save(WeekDayInfoRequestDTO weekDayInfoRequestDTO) {
        RestaurateurUser principal = (RestaurateurUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        WeekDayInfo weekDayInfo = weekDayInfoMapper.weekDayInfoRequestDTOToWeekDayInfo(weekDayInfoRequestDTO);
        Restaurant restaurant = restaurantRepository
                .findById(weekDayInfoRequestDTO.getRestaurantId())
                .orElseThrow(() -> new EntityNotFoundException("restaurant", "id", weekDayInfoRequestDTO.getRestaurantId()));

        weekDayInfo.setRestaurant(restaurant);

        checkRestaurantAccessOrThrow(principal, weekDayInfo.getRestaurant());

        try {
            weekDayInfo = weekDayInfoRepository.save(weekDayInfo);
        } catch (DataIntegrityViolationException e) {
            throw new EntityDuplicateException("week day info", "weekDay", weekDayInfo.getWeekDay(), "restaurantId", weekDayInfo.getRestaurant().getId());
        } catch (Exception e) {
            throw new EntityCreationException("week day info");
        }

        sittingTimeService.createForWeekDayInfo(weekDayInfo);

        return weekDayInfoMapper.weekDayInfoToWeekDayInfoResponseDTO(weekDayInfo);
    }

    @Override
    public List<WeekDayInfoResponseDTO> findAll() {
        List<WeekDayInfo> weekDayInfos = weekDayInfoRepository.findAll();
        return weekDayInfoMapper.weekDayInfosToWeekDayInfoResponseDTOs(weekDayInfos);
    }

    @Override
    public List<WeekDayInfoResponseDTO> findAllByRestaurant(long restaurantId) {
        List<WeekDayInfo> weekDayInfos = weekDayInfoRepository.findAllByRestaurantIdOrderByWeekDay(restaurantId);
        return weekDayInfoMapper.weekDayInfosToWeekDayInfoResponseDTOs(weekDayInfos);
    }

    @Override
    public WeekDayInfoResponseDTO update(long id, WeekDayInfoUpdateRequestDTO weekDayInfoUpdateRequestDTO) {
        RestaurateurUser principal = (RestaurateurUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
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

    private void checkRestaurantAccessOrThrow(User user, Restaurant restaurant) {
        if (restaurant.getRestaurateur().getId() == user.getId()) return;

        throw new ForbiddenRestaurantAccessException();
    }

    private void updateWeekDayInfoSittingTimes(WeekDayInfo weekDayInfo) {
        weekDayInfo.getSittingTimes().forEach(sittingTime ->
                sittingTimeService.remove(sittingTime.getId())
        );
        sittingTimeService.createForWeekDayInfo(weekDayInfo);
    }
}