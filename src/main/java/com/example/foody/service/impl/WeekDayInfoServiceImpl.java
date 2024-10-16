package com.example.foody.service.impl;

import com.example.foody.dto.request.WeekDayInfoRequestDTO;
import com.example.foody.dto.response.WeekDayInfoResponseDTO;
import com.example.foody.exceptions.entity.EntityCreationException;
import com.example.foody.exceptions.entity.EntityNotFoundException;
import com.example.foody.exceptions.restaurant.ForbiddenRestaurantAccessException;
import com.example.foody.exceptions.week_day_info.DuplicateWeekDayInfoException;
import com.example.foody.mapper.WeekDayInfoMapper;
import com.example.foody.model.Restaurant;
import com.example.foody.model.User;
import com.example.foody.model.WeekDayInfo;
import com.example.foody.repository.RestaurantRepository;
import com.example.foody.repository.WeekDayInfoRepository;
import com.example.foody.service.SittingTimeService;
import com.example.foody.service.WeekDayInfoService;
import com.example.foody.utils.Role;
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
        WeekDayInfo weekDayInfo = weekDayInfoMapper.weekDayInfoRequestDTOToWeekDayInfo(weekDayInfoRequestDTO);

        Restaurant restaurant = restaurantRepository
                .findByIdAndDeletedAtIsNull(weekDayInfoRequestDTO.getRestaurantId())
                .orElseThrow(() -> new EntityNotFoundException("restaurant", "id", weekDayInfoRequestDTO.getRestaurantId()));

        weekDayInfo.setRestaurant(restaurant);

        // Check if the principal is the owner of the week day info's restaurant
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (restaurant.getUser().getId() != principal.getId() && !principal.getRole().equals(Role.ADMIN)) {
            throw new ForbiddenRestaurantAccessException();
        }

        // Check if a week day info with the same week day and restaurant id already exists
        if (weekDayInfoRepository.existsByDeletedAtIsNullAndWeekDayAndRestaurantId(weekDayInfo.getWeekDay(), restaurant.getId())) {
            throw new DuplicateWeekDayInfoException(weekDayInfo.getWeekDay(), restaurant.getId());
        }

        try {
            weekDayInfo = weekDayInfoRepository.save(weekDayInfo);
        } catch (Exception e) {
            throw new EntityCreationException("week day info");
        }

        // Generate and save week day info's sitting times
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

        // Remove the associated sitting times
        weekDayInfo.getSittingTimes().forEach(
                sittingTime -> sittingTimeService.remove(sittingTime.getId())
        );

        try {
            weekDayInfoRepository.save(weekDayInfo);
        } catch (Exception e) {
            throw new EntityNotFoundException("week day info", "id", id);
        }

        return true;
    }
}
