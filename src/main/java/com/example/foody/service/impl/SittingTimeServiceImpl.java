package com.example.foody.service.impl;

import com.example.foody.dto.request.SittingTimeRequestDTO;
import com.example.foody.dto.response.SittingTimeResponseDTO;
import com.example.foody.exceptions.entity.EntityCreationException;
import com.example.foody.exceptions.entity.EntityDeletionException;
import com.example.foody.exceptions.entity.EntityNotFoundException;
import com.example.foody.exceptions.sitting_time.InvalidWeekDayException;
import com.example.foody.exceptions.sitting_time.SittingTimeOverlappingException;
import com.example.foody.mapper.SittingTimeMapper;
import com.example.foody.model.Restaurant;
import com.example.foody.model.SittingTime;
import com.example.foody.repository.RestaurantRepository;
import com.example.foody.repository.SittingTimeRepository;
import com.example.foody.service.SittingTimeService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(rollbackOn = Exception.class)
public class SittingTimeServiceImpl implements SittingTimeService {
    private final SittingTimeRepository sittingTimeRepository;
    private final RestaurantRepository restaurantRepository;
    private final SittingTimeMapper sittingTimeMapper;

    public SittingTimeServiceImpl(SittingTimeRepository sittingTimeRepository, RestaurantRepository restaurantRepository, SittingTimeMapper sittingTimeMapper) {
        this.sittingTimeRepository = sittingTimeRepository;
        this.restaurantRepository = restaurantRepository;
        this.sittingTimeMapper = sittingTimeMapper;
    }

    @Override
    public SittingTimeResponseDTO save(SittingTimeRequestDTO sittingTimeDTO) {
        SittingTime sittingTime = sittingTimeMapper.sittingTimeRequestDTOToSittingTime(sittingTimeDTO);
        Restaurant restaurant = restaurantRepository.findById(sittingTimeDTO.getRestaurantId())
                .orElseThrow(() -> new EntityNotFoundException("restaurant", "id", sittingTimeDTO.getRestaurantId()));

        sittingTime.setRestaurant(restaurant);

        // Check if there are any overlapping sitting times
        List<SittingTime> overlappingSittingTimes = sittingTimeRepository.findAllWithOverlappingTime(
                sittingTime.getRestaurant().getId(),
                sittingTime.getWeekDay(),
                sittingTime.getStartTime(),
                sittingTime.getEndTime()
        );

        if (!overlappingSittingTimes.isEmpty()) {
            throw new SittingTimeOverlappingException(sittingTimeDTO.getStartTime(), sittingTimeDTO.getEndTime());
        }

        try {
            sittingTime = sittingTimeRepository.save(sittingTime);
        } catch (Exception e) {
            throw new EntityCreationException("sitting time");
        }

        return sittingTimeMapper.sittingTimeToSittingTimeResponseDTO(sittingTime);
    }

    @Override
    public List<SittingTimeResponseDTO> findAll() {
        List<SittingTime> sittingTimes = sittingTimeRepository.findAllByDeletedAtIsNull();
        return sittingTimeMapper.sittingTimesToSittingTimeResponseDTOs(sittingTimes);
    }

    @Override
    public List<SittingTimeResponseDTO> findAllByRestaurant(long restaurantId) {
        restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new EntityNotFoundException("restaurant", "id", restaurantId));
        List<SittingTime> sittingTimeResponseDTOS = sittingTimeRepository.findAllByDeletedAtIsNullAndRestaurant(restaurantId);

        return sittingTimeMapper.sittingTimesToSittingTimeResponseDTOs(sittingTimeResponseDTOS);
    }

    @Override
    public List<SittingTimeResponseDTO> findAllByRestaurantAndWeekDayAndStartTimeAfterNow(long restaurantId, int weeDay) {
        restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new EntityNotFoundException("restaurant", "id", restaurantId));

        if (weeDay < 1 || weeDay > 7) throw new InvalidWeekDayException(weeDay);

        List<SittingTime> sittingTimes = sittingTimeRepository.findAllByDeletedAtIsNullAndRestaurantAndWeekDayAndStartTimeAfterNow(restaurantId, weeDay);

        return sittingTimeMapper.sittingTimesToSittingTimeResponseDTOs(sittingTimes);
    }

    @Override
    public boolean remove(long id) {
        SittingTime sittingTime = sittingTimeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("sitting time", "id", id));

        sittingTime.setDeletedAt(LocalDateTime.now());

        try {
            sittingTimeRepository.save(sittingTime);
        } catch (Exception e) {
            throw new EntityDeletionException("sitting time", "id", id);
        }

        return true;
    }
}
