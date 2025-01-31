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
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

/**
 * Implementation of the {@link BookingService} interface.
 * <p>
 * Provides methods to create, update, and delete {@link Booking} objects.
 */
@Service
@Transactional(rollbackOn = Exception.class)
@AllArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final SittingTimeRepository sittingTimeRepository;
    private final RestaurantRepository restaurantRepository;
    private final BookingMapper bookingMapper;
    private final EmailService emailService;

    /**
     * {@inheritDoc}
     * <p>
     * This method persists a new {@link Booking} entity to the database.
     *
     * @param bookingDTO the booking request data transfer object
     * @return the saved booking response data transfer object
     * @throws EntityCreationException if there is an error during the creation of the booking
     * @throws EntityNotFoundException if the sitting time or restaurant is not found
     */
    @Override
    public BookingResponseDTO save(BookingRequestDTO bookingDTO) {
        CustomerUser principal = (CustomerUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Booking booking = bookingMapper.bookingRequestDTOToBooking(bookingDTO);
        SittingTime sittingTime = sittingTimeRepository
                .findById(bookingDTO.getSittingTimeId())
                .orElseThrow(() -> new EntityNotFoundException("sitting time", "id", bookingDTO.getSittingTimeId()));
        Restaurant restaurant = restaurantRepository
                .findByIdAndApproved(bookingDTO.getRestaurantId(), true)
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

    /**
     * {@inheritDoc}
     * <p>
     * This method retrieves all {@link Booking} entities from the database.
     *
     * @return the list of booking response data transfer objects
     */
    @Override
    public List<BookingResponseDTO> findAll() {
        List<Booking> bookings = bookingRepository.findAll();
        return bookingMapper.bookingsToBookingResponseDTOs(bookings);
    }

    /**
     * {@inheritDoc}
     * <p>
     * This method retrieves a {@link Booking} entity by its ID.
     *
     * @param id the ID of the booking to retrieve
     * @return the booking response data transfer object
     * @throws EntityNotFoundException if the booking with the specified ID is not found
     */
    @Override
    public BookingResponseDTO findById(long id) {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Booking booking = bookingRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("booking", "id", id));

        checkBookingAccessOrThrow(principal, booking);

        return bookingMapper.bookingToBookingResponseDTO(booking);
    }

    /**
     * {@inheritDoc}
     * <p>
     * This method retrieves all {@link Booking} entities for a specific customer.
     *
     * @param customerId the ID of the customer
     * @return the list of booking response data transfer objects
     */
    @Override
    public List<BookingResponseDTO> findAllByCustomer(long customerId) {
        List<Booking> bookings = bookingRepository
                .findAllByCustomer_IdOrderByDateDesc(customerId);
        return bookingMapper.bookingsToBookingResponseDTOs(bookings);
    }

    /**
     * {@inheritDoc}
     * <p>
     * This method retrieves all current active {@link Booking} entities for a specific customer.
     *
     * @param customerId the ID of the customer
     * @return the list of booking response data transfer objects
     */
    @Override
    public List<BookingResponseDTO> findAllCurrentActivesByCustomer(long customerId) {
        List<Booking> bookings = bookingRepository
                .findAllCurrentActiveBookingsByCustomer_Id(customerId);
        return bookingMapper.bookingsToBookingResponseDTOs(bookings);
    }

    /**
     * {@inheritDoc}
     * <p>
     * This method retrieves all {@link Booking} entities for a specific restaurant.
     *
     * @param restaurantId the ID of the restaurant
     * @return the list of booking response data transfer objects
     * @throws EntityNotFoundException if the restaurant with the specified ID is not found
     * @throws ForbiddenRestaurantAccessException if access to the restaurant is forbidden
     */
    @Override
    public List<BookingResponseDTO> findAllByRestaurant(long restaurantId) {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Restaurant restaurant = restaurantRepository
                .findById(restaurantId)
                .orElseThrow(() -> new EntityNotFoundException("restaurant", "id", restaurantId));

        checkRestaurantAccessOrThrow(principal, restaurant);

        List<Booking> bookings = bookingRepository
                .findAllByRestaurant_IdOrderByDateDesc(restaurantId);

        return bookingMapper.bookingsToBookingResponseDTOs(bookings);
    }

    /**
     * {@inheritDoc}
     * <p>
     * This method cancels a {@link Booking} entity by its ID.
     *
     * @param id the ID of the booking to cancel
     * @return the cancelled booking response data transfer object
     * @throws EntityNotFoundException if the booking with the specified ID is not found
     * @throws InvalidBookingStateException if the booking is in an invalid state for cancellation
     * @throws EntityEditException if there is an error during the update of the booking
     */
    @Override
    public BookingResponseDTO cancelById(long id) {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Booking booking = bookingRepository
                .findById(id)
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

        sendBookingCancelledByCustomerEmail(booking);

        return bookingMapper.bookingToBookingResponseDTO(booking);
    }

    /**
     * {@inheritDoc}
     * <p>
     * This method removes a {@link Booking} entity by its ID.
     *
     * @param id the ID of the booking to remove
     * @return true if the booking was removed, false otherwise
     * @throws EntityNotFoundException if the booking with the specified ID is not found
     * @throws EntityDeletionException if there is an error during the deletion of the booking
     */
    @Override
    public boolean remove(long id) {
        Booking booking = bookingRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("booking", "id", id));
        booking.delete();

        try {
            bookingRepository.save(booking);
        } catch (Exception e) {
            throw new EntityDeletionException("booking", "id", id);
        }

        // When a sitting time is deleted, all bookings related to it are deleted
        sendBookingCancelledByRestaurantEmail(booking);

        return true;
    }

    /**
     * Checks the validity of the booking creation.
     * <p>
     * This method performs various checks to ensure the booking can be created.
     *
     * @param booking the booking to check
     * @throws InvalidBookingWeekDayException if the booking is on an invalid weekday
     * @throws InvalidBookingSittingTimeException if the booking sitting time is invalid
     * @throws InvalidBookingRestaurantException if the booking restaurant is invalid
     * @throws DuplicateActiveFutureBookingException if there is a duplicate active future booking
     * @throws BookingNotAllowedException if the booking is not allowed due to seat availability
     */
    private void checkBookingCreationOrThrow(Booking booking) {
        checkWeekDayOrThrow(booking);
        checkSittingTimeOrThrow(booking);
        checkSittingTimeIsInFutureOrThrow(booking);
        checkNoDuplicateActiveFutureBookingOrThrow(booking);
        checkSeatsAvailabilityOrThrow(booking);
    }

    /**
     * Sends an email notification for a created booking.
     * <p>
     * This method sends a templated email to the customer with booking details.
     *
     * @param booking the booking for which the email is sent
     */
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

    /**
     * Checks the validity of the booking cancellation.
     * <p>
     * This method performs various checks to ensure the booking can be cancelled.
     *
     * @param user the user attempting to cancel the booking
     * @param booking the booking to check
     * @throws ForbiddenBookingAccessException if the user does not have access to the booking
     */
    private void checkBookingCancelOrThrow(User user, Booking booking) {
        if (UserRoleUtils.isCustomer(user)) checkBookingAccessOrThrow(user, booking);
        if (UserRoleUtils.isRestaurateur(user)) checkRestaurantAccessOrThrow(user, booking.getRestaurant());
    }

    /**
     * Sends an email notification for a booking cancelled by the customer.
     * <p>
     * This method sends a templated email to the customer with cancellation details.
     *
     * @param booking the booking for which the email is sent
     */
    private void sendBookingCancelledByCustomerEmail(Booking booking) {
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
                EmailTemplateType.BOOKING_CANCELLED_BY_CUSTOMER,
                variables
        );
    }

    /**
     * Sends an email notification for a booking cancelled by the restaurant.
     * <p>
     * This method sends a templated email to the customer with cancellation details.
     *
     * @param booking the booking for which the email is sent
     */
    private void sendBookingCancelledByRestaurantEmail(Booking booking) {
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
                EmailTemplateType.BOOKING_CANCELLED_BY_RESTAURANT,
                variables
        );
    }

    /**
     * Checks if the booking is on a valid weekday.
     * <p>
     * This method throws an exception if the booking is on an invalid weekday.
     *
     * @param booking the booking to check
     * @throws InvalidBookingWeekDayException if the booking is on an invalid weekday
     */
    private void checkWeekDayOrThrow(Booking booking) {
        if (booking.getDate().getDayOfWeek().getValue() == booking.getSittingTime().getWeekDayInfo().getWeekDay())
            return;

        throw new InvalidBookingWeekDayException();
    }

    /**
     * Checks if the booking sitting time is in the future.
     * <p>
     * This method throws an exception if the booking sitting time is not in the future.
     *
     * @param booking the booking to check
     * @throws InvalidBookingSittingTimeException if the booking sitting time is not in the future
     */
    private void checkSittingTimeIsInFutureOrThrow(Booking booking) {
        if (booking.getDate().isAfter(LocalDate.now())) return;
        if (booking.getSittingTime().getStart().isAfter(LocalTime.now())) return;

        throw new InvalidBookingSittingTimeException();
    }

    /**
     * Checks if the booking restaurant is valid.
     * <p>
     * This method throws an exception if the booking restaurant is invalid.
     *
     * @param booking the booking to check
     * @throws InvalidBookingRestaurantException if the booking restaurant is invalid
     */
    private void checkSittingTimeOrThrow(Booking booking) {
        if (booking.getRestaurant().getId() == booking.getSittingTime().getWeekDayInfo().getRestaurant().getId())
            return;

        throw new InvalidBookingRestaurantException();
    }

    /**
     * Checks if there is a duplicate active future booking.
     * <p>
     * This method throws an exception if there is a duplicate active future booking.
     *
     * @param booking the booking to check
     * @throws DuplicateActiveFutureBookingException if there is a duplicate active future booking
     */
    private void checkNoDuplicateActiveFutureBookingOrThrow(Booking booking) {
        if (!bookingRepository.existsActiveFutureBookingByCustomer_IdAndRestaurant_IdAndDate(
                booking.getCustomer().getId(), booking.getRestaurant().getId(), booking.getDate())) return;

        throw new DuplicateActiveFutureBookingException(
                booking.getRestaurant().getId(),
                booking.getDate(),
                booking.getSittingTime().getId()
        );
    }

    /**
     * Checks if there are enough seats available for the booking.
     * <p>
     * This method throws an exception if there are not enough seats available.
     *
     * @param booking the booking to check
     * @throws BookingNotAllowedException if there are not enough seats available
     */
    private void checkSeatsAvailabilityOrThrow(Booking booking) {
        long bookedSeats = bookingRepository
                .countBookedSeats(booking.getDate(), booking.getSittingTime().getId(), booking.getRestaurant().getId());
        if (bookedSeats + booking.getSeats() <= booking.getRestaurant().getSeats()) return;

        throw new BookingNotAllowedException(
                booking.getRestaurant().getId(),
                booking.getDate(),
                booking.getSittingTime().getId()
        );
    }

    /**
     * Checks if the user has access to the booking.
     * <p>
     * This method throws an exception if the user does not have access to the booking.
     *
     * @param user the user to check
     * @param booking the booking to check
     * @throws ForbiddenBookingAccessException if the user does not have access to the booking
     */
    private void checkBookingAccessOrThrow(User user, Booking booking) {
        if (!UserRoleUtils.isCustomer(user) && !UserRoleUtils.isRestaurateur(user)) return;
        if (booking.getCustomer().getId() == user.getId()) return;
        if (booking.getRestaurant().getRestaurateur().getId() == user.getId()) return;

        throw new ForbiddenBookingAccessException();
    }

    /**
     * Checks if the user has access to the restaurant.
     * <p>
     * This method throws an exception if the user does not have access to the restaurant.
     *
     * @param user the user to check
     * @param restaurant the restaurant to check
     * @throws ForbiddenRestaurantAccessException if the user does not have access to the restaurant
     */
    private void checkRestaurantAccessOrThrow(User user, Restaurant restaurant) {
        if (!UserRoleUtils.isRestaurateur(user)) return;
        if (restaurant.getRestaurateur().getId() == user.getId()) return;

        throw new ForbiddenRestaurantAccessException();
    }
}