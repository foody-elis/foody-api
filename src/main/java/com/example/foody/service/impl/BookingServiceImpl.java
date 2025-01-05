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
import com.example.foody.model.user.CustomerUser;
import com.example.foody.model.user.User;
import com.example.foody.repository.BookingRepository;
import com.example.foody.repository.RestaurantRepository;
import com.example.foody.repository.SittingTimeRepository;
import com.example.foody.service.BookingService;
import com.example.foody.service.EmailService;
import com.example.foody.state.booking.impl.ActiveState;
import com.example.foody.utils.UserRoleUtils;
import com.example.foody.utils.enums.EmailPlaceholder;
import com.example.foody.utils.enums.EmailTemplateType;
import jakarta.transaction.Transactional;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
@Transactional(rollbackOn = Exception.class)
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final SittingTimeRepository sittingTimeRepository;
    private final RestaurantRepository restaurantRepository;
    private final BookingMapper bookingMapper;
    private final EmailService emailService;

    public BookingServiceImpl(BookingRepository bookingRepository, SittingTimeRepository sittingTimeRepository, RestaurantRepository restaurantRepository, BookingMapper bookingMapper, EmailService emailService) {
        this.bookingRepository = bookingRepository;
        this.sittingTimeRepository = sittingTimeRepository;
        this.restaurantRepository = restaurantRepository;
        this.bookingMapper = bookingMapper;
        this.emailService = emailService;
    }

    @Override
    public BookingResponseDTO save(BookingRequestDTO bookingDTO) {
        CustomerUser principal = (CustomerUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Booking booking = bookingMapper.bookingRequestDTOToBooking(bookingDTO);
        SittingTime sittingTime = sittingTimeRepository
                .findByIdAndDeletedAtIsNull(bookingDTO.getSittingTimeId())
                .orElseThrow(() -> new EntityNotFoundException("sitting time", "id", bookingDTO.getSittingTimeId()));
        Restaurant restaurant = restaurantRepository
                .findByIdAndDeletedAtIsNullAndApproved(bookingDTO.getRestaurantId(), true)
                .orElseThrow(() -> new EntityNotFoundException("restaurant", "id", bookingDTO.getRestaurantId()));

        booking.setSittingTime(sittingTime);
        booking.setRestaurant(restaurant);
        booking.setCustomer(principal);
        booking.setState(new ActiveState());

        checkBookingCreationOrThrow(booking);

        try {
            booking = bookingRepository.save(booking);
        } catch (Exception e) {
            throw new EntityCreationException("booking");
        }

        sendBookingCreatedEmail(booking);

        return bookingMapper.bookingToBookingResponseDTO(booking);
    }

    @Override
    public List<BookingResponseDTO> findAll() {
        List<Booking> bookings = bookingRepository.findAllByDeletedAtIsNull();
        return bookingMapper.bookingsToBookingResponseDTOs(bookings);
    }

    @Override
    public BookingResponseDTO findById(long id) {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Booking booking = bookingRepository
                .findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new EntityNotFoundException("booking", "id", id));

        checkBookingAccessOrThrow(principal, booking);

        return bookingMapper.bookingToBookingResponseDTO(booking);
    }

    @Override
    public List<BookingResponseDTO> findAllByCustomer(long customerId) {
        List<Booking> bookings = bookingRepository
                .findAllByDeletedAtIsNullAndCustomer_IdOrderByDateDesc(customerId);
        return bookingMapper.bookingsToBookingResponseDTOs(bookings);
    }

    @Override
    public List<BookingResponseDTO> findAllByRestaurant(long restaurantId) {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Restaurant restaurant = restaurantRepository
                .findByIdAndDeletedAtIsNull(restaurantId)
                .orElseThrow(() -> new EntityNotFoundException("restaurant", "id", restaurantId));

        checkRestaurantAccessOrThrow(principal, restaurant);

        List<Booking> bookings = bookingRepository
                .findAllByDeletedAtIsNullAndRestaurant_IdOrderByDateDesc(restaurantId);

        return bookingMapper.bookingsToBookingResponseDTOs(bookings);
    }

    @Override
    public BookingResponseDTO cancelById(long id) {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Booking booking = bookingRepository
                .findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new EntityNotFoundException("booking", "id", id));

        checkBookingCancelOrThrow(principal, booking);

        try {
            booking.cancel();
            booking = bookingRepository.save(booking);
        } catch (IllegalStateException e) {
            throw new InvalidBookingStateException(e.getMessage());
        } catch (Exception e) {
            throw new EntityEditException("booking", "id", id);
        }

        // Booking is deleted by the user (if it is the booking's customer) or when the associated SittingTime is deleted.
        sendBookingCancelledEmailBasedOnUserRole(principal, booking);

        return bookingMapper.bookingToBookingResponseDTO(booking);
    }

    @Override
    public boolean remove(long id) {
        Booking booking = bookingRepository
                .findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new EntityNotFoundException("booking", "id", id));
        booking.delete();

        try {
            bookingRepository.save(booking);
        } catch (Exception e) {
            throw new EntityDeletionException("booking", "id", id);
        }

        return true;
    }

    private void checkBookingCreationOrThrow(Booking booking) {
        checkWeekDayOrThrow(booking);
        checkSittingTimeOrThrow(booking);
        checkSittingTimeIsInFutureOrThrow(booking);
        checkNoDuplicateActiveFutureBookingOrThrow(booking);
        checkSeatsAvailabilityOrThrow(booking);
    }

    private void sendBookingCreatedEmail(Booking booking) {
        Map<EmailPlaceholder, Object> variables = Map.of(
                EmailPlaceholder.RESTAURANT_NAME, booking.getRestaurant().getName(),
                EmailPlaceholder.CUSTOMER_NAME, booking.getCustomer().getName(),
                EmailPlaceholder.CUSTOMER_SURNAME, booking.getCustomer().getSurname(),
                EmailPlaceholder.BOOKING_ID, booking.getId(),
                EmailPlaceholder.DATE, booking.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                EmailPlaceholder.TIME, booking.getSittingTime().getStart().format(DateTimeFormatter.ofPattern("HH:mm")),
                EmailPlaceholder.DURATION, booking.getSittingTime().getWeekDayInfo().getSittingTimeStep().getMinutes(),
                EmailPlaceholder.SEATS, booking.getSeats()
        );
        emailService.sendTemplatedEmail(
                booking.getCustomer().getEmail(),
                EmailTemplateType.BOOKING_CREATED,
                variables
        );
    }

    private void checkBookingCancelOrThrow(User user, Booking booking) {
        if (UserRoleUtils.isCustomer(user)) checkBookingAccessOrThrow(user, booking);
        if (UserRoleUtils.isRestaurateur(user)) checkRestaurantAccessOrThrow(user, booking.getRestaurant());
    }

    private void sendBookingCancelledEmailBasedOnUserRole(User user, Booking booking) {
        EmailTemplateType emailTemplateType = UserRoleUtils.isCustomer(user)
                ? EmailTemplateType.BOOKING_CANCELLED_BY_CUSTOMER
                : EmailTemplateType.BOOKING_CANCELLED_BY_RESTAURANT;
        Map<EmailPlaceholder, Object> variables = Map.of(
                EmailPlaceholder.RESTAURANT_NAME, booking.getRestaurant().getName(),
                EmailPlaceholder.CUSTOMER_NAME, booking.getCustomer().getName(),
                EmailPlaceholder.CUSTOMER_SURNAME, booking.getCustomer().getSurname(),
                EmailPlaceholder.BOOKING_ID, booking.getId(),
                EmailPlaceholder.DATE, booking.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                EmailPlaceholder.TIME, booking.getSittingTime().getStart().format(DateTimeFormatter.ofPattern("HH:mm"))
        );
        emailService.sendTemplatedEmail(
                booking.getCustomer().getEmail(),
                emailTemplateType,
                variables
        );
    }

    private void checkWeekDayOrThrow(Booking booking) {
        if (booking.getDate().getDayOfWeek().getValue() == booking.getSittingTime().getWeekDayInfo().getWeekDay()) return;

        throw new InvalidBookingWeekDayException();
    }

    private void checkSittingTimeIsInFutureOrThrow(Booking booking) {
        if (booking.getDate().isAfter(LocalDate.now())) return;
        if (booking.getSittingTime().getStart().isAfter(LocalTime.now())) return;

        throw new InvalidBookingSittingTimeException();
    }

    private void checkSittingTimeOrThrow(Booking booking) {
        if (booking.getRestaurant().getId() == booking.getSittingTime().getWeekDayInfo().getRestaurant().getId()) return;

        throw new InvalidBookingRestaurantException();
    }

    private void checkNoDuplicateActiveFutureBookingOrThrow(Booking booking) {
        if (!bookingRepository.existsActiveFutureBookingByCustomer_IdAndRestaurant_IdAndDate(
                booking.getCustomer().getId(), booking.getRestaurant().getId(), booking.getDate())) return;

        throw new DuplicateActiveFutureBookingException(booking.getRestaurant().getId(), booking.getDate(), booking.getSittingTime().getId());
    }

    private void checkSeatsAvailabilityOrThrow(Booking booking) {
        long bookedSeats = bookingRepository
                .countBookedSeats(booking.getDate(), booking.getSittingTime().getId(), booking.getRestaurant().getId());
        if (bookedSeats + booking.getSeats() <= booking.getRestaurant().getSeats()) return;

        throw new BookingNotAllowedException(booking.getRestaurant().getId(), booking.getDate(), booking.getSittingTime().getId());
    }

    private void checkBookingAccessOrThrow(User user, Booking booking) {
        if (!UserRoleUtils.isCustomer(user) && !UserRoleUtils.isRestaurateur(user)) return;
        if (booking.getCustomer().getId() == user.getId()) return;
        if (booking.getRestaurant().getRestaurateur().getId() == user.getId()) return;

        throw new ForbiddenBookingAccessException();
    }

    private void checkRestaurantAccessOrThrow(User user, Restaurant restaurant) {
        if (!UserRoleUtils.isRestaurateur(user)) return;
        if (restaurant.getRestaurateur().getId() == user.getId()) return;

        throw new ForbiddenRestaurantAccessException();
    }
}