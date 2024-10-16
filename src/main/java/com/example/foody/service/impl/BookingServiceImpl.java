package com.example.foody.service.impl;

import com.example.foody.dto.request.BookingRequestDTO;
import com.example.foody.dto.response.BookingResponseDTO;
import com.example.foody.exceptions.booking.*;
import com.example.foody.exceptions.entity.EntityCreationException;
import com.example.foody.exceptions.entity.EntityDeletionException;
import com.example.foody.exceptions.entity.EntityEditException;
import com.example.foody.exceptions.entity.EntityNotFoundException;
import com.example.foody.exceptions.restaurant.ForbiddenRestaurantAccessException;
import com.example.foody.mapper.BookingMapper;
import com.example.foody.model.Booking;
import com.example.foody.model.Restaurant;
import com.example.foody.model.SittingTime;
import com.example.foody.model.User;
import com.example.foody.repository.BookingRepository;
import com.example.foody.repository.RestaurantRepository;
import com.example.foody.repository.SittingTimeRepository;
import com.example.foody.repository.UserRepository;
import com.example.foody.service.BookingService;
import com.example.foody.state.booking.ActiveState;
import com.example.foody.utils.Role;
import jakarta.transaction.Transactional;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(rollbackOn = Exception.class)
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final SittingTimeRepository sittingTimeRepository;
    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;
    private final BookingMapper bookingMapper;

    public BookingServiceImpl(BookingRepository bookingRepository, SittingTimeRepository sittingTimeRepository, UserRepository userRepository, RestaurantRepository restaurantRepository, BookingMapper bookingMapper) {
        this.bookingRepository = bookingRepository;
        this.sittingTimeRepository = sittingTimeRepository;
        this.userRepository = userRepository;
        this.restaurantRepository = restaurantRepository;
        this.bookingMapper = bookingMapper;
    }

    @Override
    public BookingResponseDTO save(BookingRequestDTO bookingDTO) {
        Booking booking = bookingMapper.bookingRequestDTOToBooking(bookingDTO);

        SittingTime sittingTime = sittingTimeRepository
                .findByIdAndDeletedAtIsNull(bookingDTO.getSittingTimeId())
                .orElseThrow(() -> new EntityNotFoundException("sitting time", "id", bookingDTO.getSittingTimeId()));

        Restaurant restaurant = restaurantRepository
                .findByIdAndDeletedAtIsNullAndApproved(bookingDTO.getRestaurantId(), true)
                .orElseThrow(() -> new EntityNotFoundException("restaurant", "id", bookingDTO.getRestaurantId()));

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        booking.setSittingTime(sittingTime);
        booking.setRestaurant(restaurant);
        booking.setUser(user);
        booking.setState(new ActiveState(booking));

        // Check if the booking week day is the same as the sitting time week day
        if (booking.getDate().getDayOfWeek().getValue() != booking.getSittingTime().getWeekDayInfo().getWeekDay()) {
            throw new InvalidBookingWeekDayException();
        }

        // Check if the booking restaurant is the same as the sitting time restaurant
        if (booking.getRestaurant().getId() != booking.getSittingTime().getWeekDayInfo().getRestaurant().getId()) {
            throw new InvalidBookingRestaurantException();
        }

        // Check if the restaurant has enough seats available
        long bookedSeats = bookingRepository
                .countBookedSeats(bookingDTO.getDate(), bookingDTO.getSittingTimeId(), restaurant.getId());

        if (bookedSeats + bookingDTO.getSeats() > restaurant.getSeats()) {
            throw new BookingNotAllowedException(bookingDTO.getRestaurantId(), bookingDTO.getDate(), bookingDTO.getSittingTimeId());
        }

        try {
            booking = bookingRepository.save(booking);
        } catch (Exception e) {
            throw new EntityCreationException("booking");
        }

        // todo send email to user

        return bookingMapper.bookingToBookingResponseDTO(booking);
    }

    @Override
    public List<BookingResponseDTO> findAll() {
        List<Booking> bookings = bookingRepository.findAllByDeletedAtIsNull();
        return bookingMapper.bookingsToBookingResponseDTOs(bookings);
    }

    @Override
    public BookingResponseDTO findById(long id) {
        Booking booking = bookingRepository
                .findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new EntityNotFoundException("booking", "id", id));

        // Check if the user is the owner of the booking or an admin
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (booking.getUser().getId() != principal.getId() && !principal.getRole().equals(Role.ADMIN)) {
            throw new ForbiddenBookingAccessException();
        }

        return bookingMapper.bookingToBookingResponseDTO(booking);
    }

    @Override
    public List<BookingResponseDTO> findAllByUser(long userId) {
        userRepository
                .findByIdAndDeletedAtIsNull(userId)
                .orElseThrow(() -> new EntityNotFoundException("user", "id", userId));

        List<Booking> bookings = bookingRepository
                .findAllByDeletedAtIsNullAndUserOrderByDateDesc(userId);

        return bookingMapper.bookingsToBookingResponseDTOs(bookings);
    }

    @Override
    public List<BookingResponseDTO> findAllByRestaurant(long restaurantId) {
        Restaurant restaurant = restaurantRepository
                .findByIdAndDeletedAtIsNull(restaurantId)
                .orElseThrow(() -> new EntityNotFoundException("restaurant", "id", restaurantId));

        // Check if the principal is the owner of the restaurant or an admin
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (restaurant.getUser().getId() != principal.getId() && !principal.getRole().equals(Role.ADMIN)) {
            throw new ForbiddenRestaurantAccessException();
        }

        List<Booking> bookings = bookingRepository
                .findAllByDeletedAtIsNullAndRestaurantOrderByDateDesc(restaurantId);

        return bookingMapper.bookingsToBookingResponseDTOs(bookings);
    }

    @Override
    public BookingResponseDTO cancelById(long id) {
        Booking booking = bookingRepository
                .findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new EntityNotFoundException("booking", "id", id));

        // Check if the user is the owner of the booking or an admin
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (booking.getUser().getId() != principal.getId() && !principal.getRole().equals(Role.ADMIN)) {
            throw new ForbiddenBookingAccessException();
        }

        try {
            booking.cancel();
            booking = bookingRepository.save(booking);
        } catch (IllegalStateException e) {
            throw new InvalidBookingStateException(e.getMessage());
        } catch (Exception e) {
            throw new EntityEditException("booking", "id", id);
        }

        // todo send email to user and restaurant (???)

        return bookingMapper.bookingToBookingResponseDTO(booking);
    }

    @Override
    public boolean remove(long id) {
        Booking booking = bookingRepository
                .findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new EntityNotFoundException("booking", "id", id));

        booking.setDeletedAt(LocalDateTime.now());

        try {
            bookingRepository.save(booking);
        } catch (Exception e) {
            throw new EntityDeletionException("booking", "id", id);
        }

        return true;
    }
}
