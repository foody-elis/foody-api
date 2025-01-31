package com.example.foody.service.impl;

import com.example.foody.TestDataUtil;
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
import com.example.foody.model.user.AdminUser;
import com.example.foody.model.user.CustomerUser;
import com.example.foody.model.user.RestaurateurUser;
import com.example.foody.model.user.User;
import com.example.foody.repository.BookingRepository;
import com.example.foody.repository.RestaurantRepository;
import com.example.foody.repository.SittingTimeRepository;
import com.example.foody.service.EmailService;
import com.example.foody.state.booking.impl.CancelledState;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link BookingServiceImpl} class using mock services.
 */
@ExtendWith(MockitoExtension.class)
public class BookingServiceImplTest {

    @InjectMocks
    private BookingServiceImpl bookingService;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private SittingTimeRepository sittingTimeRepository;

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private BookingMapper bookingMapper;

    @Mock
    private EmailService emailService;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    private void mockSecurityContext(User user) {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getPrincipal()).thenReturn(user);
    }

    @Test
    void saveWhenBookingIsValidReturnsBookingResponseDTO() {
        // Arrange
        CustomerUser customer = TestDataUtil.createTestCustomerUser();
        BookingRequestDTO bookingRequestDTO = TestDataUtil.createTestBookingRequestDTO();
        Booking booking = TestDataUtil.createTestBooking();
        SittingTime sittingTime = TestDataUtil.createTestSittingTime();
        Restaurant restaurant = TestDataUtil.createTestRestaurant();
        sittingTime.getWeekDayInfo().setWeekDay(LocalDate.now().getDayOfWeek().getValue());
        sittingTime.setStart(LocalTime.now().plusMinutes(1));
        booking.setSittingTime(sittingTime);
        booking.setDate(LocalDate.now());
        mockSecurityContext(customer);

        when(sittingTimeRepository.findById(bookingRequestDTO.getSittingTimeId())).thenReturn(Optional.of(sittingTime));
        when(restaurantRepository.findByIdAndApproved(bookingRequestDTO.getRestaurantId(), true))
                .thenReturn(Optional.of(restaurant));
        when(bookingMapper.bookingRequestDTOToBooking(bookingRequestDTO)).thenReturn(booking);
        when(bookingRepository.save(booking)).thenReturn(booking);
        when(bookingMapper.bookingToBookingResponseDTO(booking)).thenReturn(new BookingResponseDTO());

        // Act
        BookingResponseDTO responseDTO = bookingService.save(bookingRequestDTO);

        // Assert
        assertNotNull(responseDTO);
        verify(bookingRepository, times(1)).save(booking);
        verify(emailService, times(1)).sendTemplatedEmail(any(), any(), any());
    }

    @Test
    void saveWhenSittingTimeDoesNotExistThrowsEntityNotFoundException() {
        // Arrange
        CustomerUser customer = TestDataUtil.createTestCustomerUser();
        BookingRequestDTO bookingRequestDTO = TestDataUtil.createTestBookingRequestDTO();
        mockSecurityContext(customer);

        when(sittingTimeRepository.findById(bookingRequestDTO.getSittingTimeId())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> bookingService.save(bookingRequestDTO));
    }

    @Test
    void saveWhenRestaurantIsNotApprovedThrowsEntityNotFoundException() {
        // Arrange
        CustomerUser customer = TestDataUtil.createTestCustomerUser();
        BookingRequestDTO bookingRequestDTO = TestDataUtil.createTestBookingRequestDTO();
        SittingTime sittingTime = TestDataUtil.createTestSittingTime();
        Restaurant restaurant = TestDataUtil.createTestRestaurant();
        restaurant.setApproved(false);
        mockSecurityContext(customer);

        when(sittingTimeRepository.findById(bookingRequestDTO.getSittingTimeId())).thenReturn(Optional.of(sittingTime));
        when(restaurantRepository.findByIdAndApproved(bookingRequestDTO.getRestaurantId(), true))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> bookingService.save(bookingRequestDTO));
    }

    @Test
    void saveWhenSaveFailsThrowsEntityCreationException() {
        // Arrange
        CustomerUser customer = TestDataUtil.createTestCustomerUser();
        BookingRequestDTO bookingRequestDTO = TestDataUtil.createTestBookingRequestDTO();
        Booking booking = TestDataUtil.createTestBooking();
        SittingTime sittingTime = TestDataUtil.createTestSittingTime();
        Restaurant restaurant = TestDataUtil.createTestRestaurant();
        mockSecurityContext(customer);

        when(sittingTimeRepository.findById(bookingRequestDTO.getSittingTimeId())).thenReturn(Optional.of(sittingTime));
        when(restaurantRepository.findByIdAndApproved(bookingRequestDTO.getRestaurantId(), true))
                .thenReturn(Optional.of(restaurant));
        when(bookingMapper.bookingRequestDTOToBooking(bookingRequestDTO)).thenReturn(booking);
        doThrow(new RuntimeException()).when(bookingRepository).save(booking);

        // Act & Assert
        assertThrows(EntityCreationException.class, () -> bookingService.save(bookingRequestDTO));
    }

    @Test
    void saveWhenBookingWeekDayDoesNotMatchThrowsInvalidBookingWeekDayException() {
        // Arrange
        CustomerUser customer = TestDataUtil.createTestCustomerUser();
        BookingRequestDTO bookingRequestDTO = TestDataUtil.createTestBookingRequestDTO();
        Booking booking = TestDataUtil.createTestBooking();
        SittingTime sittingTime = TestDataUtil.createTestSittingTime();
        Restaurant restaurant = TestDataUtil.createTestRestaurant();
        sittingTime.getWeekDayInfo().setWeekDay((LocalDate.now().getDayOfWeek().getValue() % 7) + 2);
        booking.setSittingTime(sittingTime);
        mockSecurityContext(customer);

        when(sittingTimeRepository.findById(bookingRequestDTO.getSittingTimeId())).thenReturn(Optional.of(sittingTime));
        when(restaurantRepository.findByIdAndApproved(bookingRequestDTO.getRestaurantId(), true))
                .thenReturn(Optional.of(restaurant));
        when(bookingMapper.bookingRequestDTOToBooking(bookingRequestDTO)).thenReturn(booking);

        // Act & Assert
        assertThrows(InvalidBookingWeekDayException.class, () -> bookingService.save(bookingRequestDTO));
    }

    @Test
    void saveWhenBookingDateIsInFutureDoesNotThrowException() {
        // Arrange
        CustomerUser customer = TestDataUtil.createTestCustomerUser();
        BookingRequestDTO bookingRequestDTO = TestDataUtil.createTestBookingRequestDTO();
        Booking booking = TestDataUtil.createTestBooking();
        SittingTime sittingTime = TestDataUtil.createTestSittingTime();
        Restaurant restaurant = TestDataUtil.createTestRestaurant();
        booking.setDate(LocalDate.now().plusDays(1));
        sittingTime.getWeekDayInfo().setWeekDay(booking.getDate().getDayOfWeek().getValue());
        sittingTime.setStart(LocalTime.now().minusHours(1));
        booking.setSittingTime(sittingTime);
        mockSecurityContext(customer);

        when(sittingTimeRepository.findById(bookingRequestDTO.getSittingTimeId())).thenReturn(Optional.of(sittingTime));
        when(restaurantRepository.findByIdAndApproved(bookingRequestDTO.getRestaurantId(), true))
                .thenReturn(Optional.of(restaurant));
        when(bookingMapper.bookingRequestDTOToBooking(bookingRequestDTO)).thenReturn(booking);
        when(bookingRepository.save(booking)).thenReturn(booking);
        when(bookingMapper.bookingToBookingResponseDTO(booking)).thenReturn(new BookingResponseDTO());

        // Act
        BookingResponseDTO responseDTO = bookingService.save(bookingRequestDTO);

        // Assert
        assertNotNull(responseDTO);
        verify(bookingRepository, times(1)).save(booking);
    }

    @Test
    void saveWhenSittingTimeIsInPastThrowsInvalidBookingSittingTimeException() {
        // Arrange
        CustomerUser customer = TestDataUtil.createTestCustomerUser();
        BookingRequestDTO bookingRequestDTO = TestDataUtil.createTestBookingRequestDTO();
        Booking booking = TestDataUtil.createTestBooking();
        SittingTime sittingTime = TestDataUtil.createTestSittingTime();
        Restaurant restaurant = TestDataUtil.createTestRestaurant();
        sittingTime.getWeekDayInfo().setWeekDay(LocalDate.now().getDayOfWeek().getValue());
        sittingTime.setStart(LocalTime.now().minusHours(2));
        booking.setSittingTime(sittingTime);
        booking.setDate(LocalDate.now());
        mockSecurityContext(customer);

        when(sittingTimeRepository.findById(bookingRequestDTO.getSittingTimeId())).thenReturn(Optional.of(sittingTime));
        when(restaurantRepository.findByIdAndApproved(bookingRequestDTO.getRestaurantId(), true))
                .thenReturn(Optional.of(restaurant));
        when(bookingMapper.bookingRequestDTOToBooking(bookingRequestDTO)).thenReturn(booking);

        // Act & Assert
        assertThrows(InvalidBookingSittingTimeException.class, () -> bookingService.save(bookingRequestDTO));
    }

    @Test
    void saveWhenSittingTimeRestaurantDoesNotMatchThrowsInvalidBookingRestaurantException() {
        // Arrange
        CustomerUser customer = TestDataUtil.createTestCustomerUser();
        BookingRequestDTO bookingRequestDTO = TestDataUtil.createTestBookingRequestDTO();
        Booking booking = TestDataUtil.createTestBooking();
        SittingTime sittingTime = TestDataUtil.createTestSittingTime();
        Restaurant restaurant = TestDataUtil.createTestRestaurant();
        Restaurant differentRestaurant = TestDataUtil.createTestRestaurant();
        differentRestaurant.setId(restaurant.getId() + 1);
        sittingTime.getWeekDayInfo().setRestaurant(differentRestaurant);
        booking.setSittingTime(sittingTime);
        mockSecurityContext(customer);

        when(sittingTimeRepository.findById(bookingRequestDTO.getSittingTimeId())).thenReturn(Optional.of(sittingTime));
        when(restaurantRepository.findByIdAndApproved(bookingRequestDTO.getRestaurantId(), true))
                .thenReturn(Optional.of(restaurant));
        when(bookingMapper.bookingRequestDTOToBooking(bookingRequestDTO)).thenReturn(booking);

        // Act & Assert
        assertThrows(InvalidBookingRestaurantException.class, () -> bookingService.save(bookingRequestDTO));
    }

    @Test
    void saveWhenDuplicateActiveFutureBookingExistsThrowsDuplicateActiveFutureBookingException() {
        // Arrange
        CustomerUser customer = TestDataUtil.createTestCustomerUser();
        BookingRequestDTO bookingRequestDTO = TestDataUtil.createTestBookingRequestDTO();
        Booking booking = TestDataUtil.createTestBooking();
        SittingTime sittingTime = TestDataUtil.createTestSittingTime();
        Restaurant restaurant = TestDataUtil.createTestRestaurant();
        mockSecurityContext(customer);

        when(sittingTimeRepository.findById(bookingRequestDTO.getSittingTimeId())).thenReturn(Optional.of(sittingTime));
        when(restaurantRepository.findByIdAndApproved(bookingRequestDTO.getRestaurantId(), true))
                .thenReturn(Optional.of(restaurant));
        when(bookingMapper.bookingRequestDTOToBooking(bookingRequestDTO)).thenReturn(booking);
        when(bookingRepository
                .existsActiveFutureBookingByCustomer_IdAndRestaurant_IdAndDate(
                        customer.getId(),
                        restaurant.getId(),
                        booking.getDate())
        ).thenReturn(true);

        // Act & Assert
        assertThrows(DuplicateActiveFutureBookingException.class, () -> bookingService.save(bookingRequestDTO));
    }

    @Test
    void saveWhenSeatsAreUnavailableThrowsBookingNotAllowedException() {
        // Arrange
        CustomerUser customer = TestDataUtil.createTestCustomerUser();
        BookingRequestDTO bookingRequestDTO = TestDataUtil.createTestBookingRequestDTO();
        Booking booking = TestDataUtil.createTestBooking();
        SittingTime sittingTime = TestDataUtil.createTestSittingTime();
        Restaurant restaurant = TestDataUtil.createTestRestaurant();
        mockSecurityContext(customer);

        when(sittingTimeRepository.findById(bookingRequestDTO.getSittingTimeId())).thenReturn(Optional.of(sittingTime));
        when(restaurantRepository.findByIdAndApproved(bookingRequestDTO.getRestaurantId(), true))
                .thenReturn(Optional.of(restaurant));
        when(bookingMapper.bookingRequestDTOToBooking(bookingRequestDTO)).thenReturn(booking);
        when(bookingRepository.countBookedSeats(booking.getDate(), sittingTime.getId(), restaurant.getId()))
                .thenReturn((long) restaurant.getSeats());

        // Act & Assert
        assertThrows(BookingNotAllowedException.class, () -> bookingService.save(bookingRequestDTO));
    }

    @Test
    void findAllWhenBookingsExistReturnsBookingResponseDTOs() {
        // Arrange
        List<Booking> bookings = List.of(TestDataUtil.createTestBooking());

        when(bookingRepository.findAll()).thenReturn(bookings);
        when(bookingMapper.bookingsToBookingResponseDTOs(bookings))
                .thenReturn(List.of(TestDataUtil.createTestBookingResponseDTO()));

        // Act
        List<BookingResponseDTO> responseDTOs = bookingService.findAll();

        // Assert
        assertNotNull(responseDTOs);
        assertEquals(1, responseDTOs.size());
    }

    @Test
    void findByIdWhenUserIsCustomerReturnsBookingResponseDTO() {
        // Arrange
        Booking booking = TestDataUtil.createTestBooking();
        CustomerUser customer = TestDataUtil.createTestCustomerUser();
        mockSecurityContext(customer);

        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(booking));
        when(bookingMapper.bookingToBookingResponseDTO(booking))
                .thenReturn(TestDataUtil.createTestBookingResponseDTO());

        // Act
        BookingResponseDTO responseDTO = bookingService.findById(booking.getId());

        // Assert
        assertNotNull(responseDTO);
        verify(bookingRepository, times(1)).findById(booking.getId());
    }

    @Test
    void findByIdWhenUserIsRestaurateurReturnsBookingResponseDTO() {
        // Arrange
        Booking booking = TestDataUtil.createTestBooking();
        RestaurateurUser restaurateur = TestDataUtil.createTestRestaurateurUser();
        Restaurant restaurant = TestDataUtil.createTestRestaurant();
        restaurateur.setId(2L);
        restaurant.setRestaurateur(restaurateur);
        booking.setRestaurant(restaurant);
        mockSecurityContext(restaurateur);

        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(booking));
        when(bookingMapper.bookingToBookingResponseDTO(booking))
                .thenReturn(TestDataUtil.createTestBookingResponseDTO());

        // Act
        BookingResponseDTO responseDTO = bookingService.findById(booking.getId());

        // Assert
        assertNotNull(responseDTO);
        verify(bookingRepository, times(1)).findById(booking.getId());
    }

    @Test
    void findByIdWhenBookingDoesNotExistThrowsEntityNotFoundException() {
        // Arrange
        when(bookingRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> bookingService.findById(1L));
    }

    @Test
    void findByIdWhenUserIsNotCustomerNorRestaurateurReturnsBookingResponseDTO() {
        // Arrange
        AdminUser admin = TestDataUtil.createTestAdminUser();
        Booking booking = TestDataUtil.createTestBooking();
        mockSecurityContext(admin);

        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(booking));
        when(bookingMapper.bookingToBookingResponseDTO(booking))
                .thenReturn(TestDataUtil.createTestBookingResponseDTO());

        // Act
        BookingResponseDTO responseDTO = bookingService.findById(booking.getId());

        // Assert
        assertNotNull(responseDTO);
        verify(bookingRepository, times(1)).findById(booking.getId());
    }

    @Test
    void findByIdWhenCustomerIsNotBookingOwnerThrowsForbiddenBookingAccessException() {
        // Arrange
        Booking booking = TestDataUtil.createTestBooking();
        CustomerUser otherCustomer = TestDataUtil.createTestCustomerUser();
        otherCustomer.setId(2L);
        mockSecurityContext(otherCustomer);

        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(booking));

        // Act & Assert
        assertThrows(ForbiddenBookingAccessException.class, () -> bookingService.findById(booking.getId()));
    }

    @Test
    void findByIdWhenRestaurateurIsNotRestaurantOwnerThrowsForbiddenBookingAccessException() {
        // Arrange
        Booking booking = TestDataUtil.createTestBooking();
        RestaurateurUser otherRestaurateur = TestDataUtil.createTestRestaurateurUser();
        otherRestaurateur.setId(2L);
        mockSecurityContext(otherRestaurateur);

        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(booking));

        // Act & Assert
        assertThrows(ForbiddenBookingAccessException.class, () -> bookingService.findById(booking.getId()));
    }

    @Test
    void findAllByCustomerWhenBookingsExistReturnsBookingResponseDTOs() {
        // Arrange
        CustomerUser customer = TestDataUtil.createTestCustomerUser();
        List<Booking> bookings = List.of(TestDataUtil.createTestBooking());

        when(bookingRepository.findAllByCustomer_IdOrderByDateDesc(customer.getId())).thenReturn(bookings);
        when(bookingMapper.bookingsToBookingResponseDTOs(bookings))
                .thenReturn(List.of(TestDataUtil.createTestBookingResponseDTO()));

        // Act
        List<BookingResponseDTO> responseDTOs = bookingService.findAllByCustomer(customer.getId());

        // Assert
        assertNotNull(responseDTOs);
        assertEquals(1, responseDTOs.size());
    }

    @Test
    void findAllCurrentActivesByCustomerWhenBookingsExistReturnsBookingResponseDTOs() {
        // Arrange
        CustomerUser customer = TestDataUtil.createTestCustomerUser();
        List<Booking> bookings = List.of(TestDataUtil.createTestBooking());

        when(bookingRepository.findAllCurrentActiveBookingsByCustomer_Id(customer.getId())).thenReturn(bookings);
        when(bookingMapper.bookingsToBookingResponseDTOs(bookings))
                .thenReturn(List.of(TestDataUtil.createTestBookingResponseDTO()));

        // Act
        List<BookingResponseDTO> responseDTOs = bookingService.findAllCurrentActivesByCustomer(customer.getId());

        // Assert
        assertNotNull(responseDTOs);
        assertEquals(1, responseDTOs.size());
    }

    @Test
    void findAllByRestaurantWhenBookingsExistReturnsBookingResponseDTOs() {
        // Arrange
        RestaurateurUser restaurateur = TestDataUtil.createTestRestaurateurUser();
        Restaurant restaurant = TestDataUtil.createTestRestaurant();
        List<Booking> bookings = List.of(TestDataUtil.createTestBooking());
        mockSecurityContext(restaurateur);

        when(restaurantRepository.findById(restaurant.getId())).thenReturn(Optional.of(restaurant));
        when(bookingRepository.findAllByRestaurant_IdOrderByDateDesc(restaurant.getId())).thenReturn(bookings);
        when(bookingMapper.bookingsToBookingResponseDTOs(bookings))
                .thenReturn(List.of(TestDataUtil.createTestBookingResponseDTO()));

        // Act
        List<BookingResponseDTO> responseDTOs = bookingService.findAllByRestaurant(restaurant.getId());

        // Assert
        assertNotNull(responseDTOs);
        assertEquals(1, responseDTOs.size());
    }

    @Test
    void findAllByRestaurantWhenUserIsNotRestaurateurReturnsBookingResponseDTOs() {
        // Arrange
        AdminUser admin = TestDataUtil.createTestAdminUser();
        Restaurant restaurant = TestDataUtil.createTestRestaurant();
        List<Booking> bookings = List.of(TestDataUtil.createTestBooking());
        mockSecurityContext(admin);

        when(restaurantRepository.findById(restaurant.getId())).thenReturn(Optional.of(restaurant));
        when(bookingRepository.findAllByRestaurant_IdOrderByDateDesc(1L)).thenReturn(bookings);
        when(bookingMapper.bookingsToBookingResponseDTOs(bookings))
                .thenReturn(List.of(TestDataUtil.createTestBookingResponseDTO()));

        // Act
        List<BookingResponseDTO> responseDTOs = bookingService.findAllByRestaurant(restaurant.getId());

        // Assert
        assertNotNull(responseDTOs);
        assertEquals(1, responseDTOs.size());
    }

    @Test
    void cancelByIdWhenBookingExistsCancelsBookingAndReturnsResponseDTO() {
        // Arrange
        Booking booking = TestDataUtil.createTestBooking();
        CustomerUser customer = TestDataUtil.createTestCustomerUser();
        mockSecurityContext(customer);

        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(booking));
        when(bookingRepository.save(booking)).thenReturn(booking);
        when(bookingMapper.bookingToBookingResponseDTO(booking))
                .thenReturn(TestDataUtil.createTestBookingResponseDTO());

        // Act
        BookingResponseDTO responseDTO = bookingService.cancelById(booking.getId());

        // Assert
        assertNotNull(responseDTO);
        verify(bookingRepository, times(1)).save(booking);
    }

    @Test
    void cancelByIdWhenBookingDoesNotExistThrowsEntityNotFoundException() {
        // Arrange
        CustomerUser customer = TestDataUtil.createTestCustomerUser();
        mockSecurityContext(customer);

        when(bookingRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> bookingService.cancelById(1L));
    }

    @Test
    void cancelByIdWhenUserIsNotRestaurateurReturnsBookingResponseDTO() {
        // Arrange
        AdminUser admin = TestDataUtil.createTestAdminUser();
        Booking booking = TestDataUtil.createTestBooking();
        mockSecurityContext(admin);

        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(booking));
        when(bookingRepository.save(booking)).thenReturn(booking);
        when(bookingMapper.bookingToBookingResponseDTO(booking))
                .thenReturn(TestDataUtil.createTestBookingResponseDTO());

        // Act
        BookingResponseDTO responseDTO = bookingService.cancelById(booking.getId());

        // Assert
        assertNotNull(responseDTO);
        verify(bookingRepository, times(1)).save(booking);
    }

    @Test
    void cancelByIdWhenRestaurateurIsNotRestaurantOwnerThrowsForbiddenRestaurantAccessException() {
        // Arrange
        Booking booking = TestDataUtil.createTestBooking();
        RestaurateurUser restaurateur = TestDataUtil.createTestRestaurateurUser();
        RestaurateurUser otherRestaurateur = TestDataUtil.createTestRestaurateurUser();
        Restaurant restaurant = TestDataUtil.createTestRestaurant();
        otherRestaurateur.setId(2L);
        restaurant.setRestaurateur(otherRestaurateur);
        booking.setRestaurant(restaurant);
        mockSecurityContext(restaurateur);

        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(booking));

        // Act & Assert
        assertThrows(ForbiddenRestaurantAccessException.class, () -> bookingService.cancelById(booking.getId()));
    }

    @Test
    void cancelByIdWhenBookingIsAlreadyCancelledThrowsInvalidBookingStateException() {
        // Arrange
        CustomerUser customer = TestDataUtil.createTestCustomerUser();
        Booking booking = TestDataUtil.createTestBooking();
        booking.setState(new CancelledState());
        mockSecurityContext(customer);

        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(booking));

        // Act & Assert
        assertThrows(InvalidBookingStateException.class, () -> bookingService.cancelById(booking.getId()));
    }

    @Test
    void cancelByIdWhenSaveFailsThrowsEntityEditException() {
        // Arrange
        Booking booking = TestDataUtil.createTestBooking();
        CustomerUser customer = TestDataUtil.createTestCustomerUser();
        mockSecurityContext(customer);

        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(booking));
        doThrow(new RuntimeException()).when(bookingRepository).save(booking);

        // Act & Assert
        assertThrows(EntityEditException.class, () -> bookingService.cancelById(booking.getId()));
    }

    @Test
    void removeWhenBookingExistsDeletesBooking() {
        // Arrange
        Booking booking = TestDataUtil.createTestBooking();

        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(booking));

        // Act
        boolean result = bookingService.remove(booking.getId());

        // Assert
        assertTrue(result);
        verify(bookingRepository, times(1)).save(booking);
        verify(emailService, times(1)).sendTemplatedEmail(any(), any(), any());
    }

    @Test
    void removeWhenBookingDoesNotExistThrowsEntityNotFoundException() {
        // Arrange
        when(bookingRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> bookingService.remove(1L));
    }

    @Test
    void removeWhenSaveFailsThrowsEntityDeletionException() {
        // Arrange
        Booking booking = TestDataUtil.createTestBooking();

        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(booking));
        doThrow(new RuntimeException()).when(bookingRepository).save(booking);

        // Act & Assert
        assertThrows(EntityDeletionException.class, () -> bookingService.remove(booking.getId()));
    }
}