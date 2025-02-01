package com.example.foody.service.impl;

import com.example.foody.TestDataUtil;
import com.example.foody.dto.request.ReviewRequestDTO;
import com.example.foody.dto.response.ReviewResponseDTO;
import com.example.foody.exceptions.entity.EntityCreationException;
import com.example.foody.exceptions.entity.EntityDeletionException;
import com.example.foody.exceptions.entity.EntityNotFoundException;
import com.example.foody.exceptions.review.ForbiddenReviewAccessException;
import com.example.foody.exceptions.review.ReviewNotAllowedException;
import com.example.foody.mapper.ReviewMapper;
import com.example.foody.model.Dish;
import com.example.foody.model.Restaurant;
import com.example.foody.model.Review;
import com.example.foody.model.user.*;
import com.example.foody.observer.manager.EventManager;
import com.example.foody.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link ReviewServiceImpl} class using mock services.
 */
@ExtendWith(MockitoExtension.class)
public class ReviewServiceImplTest {

    @InjectMocks
    private ReviewServiceImpl reviewService;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private DishRepository dishRepository;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ReviewMapper reviewMapper;

    @Mock
    private EventManager eventManager;

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
    void saveWhenValidReviewReturnsReviewResponseDTO() {
        // Arrange
        CustomerUser customer = TestDataUtil.createTestCustomerUser();
        ReviewRequestDTO reviewRequestDTO = TestDataUtil.createTestReviewRequestDTO();
        Review review = TestDataUtil.createTestReview();
        Restaurant restaurant = TestDataUtil.createTestRestaurant();
        mockSecurityContext(customer);

        when(reviewMapper.reviewRequestDTOToReview(reviewRequestDTO)).thenReturn(review);
        when(restaurantRepository.findByIdAndApproved(reviewRequestDTO.getRestaurantId(), true))
                .thenReturn(Optional.of(restaurant));
        when(dishRepository.findById(reviewRequestDTO.getDishId()))
                .thenReturn(Optional.of(TestDataUtil.createTestDish()));
        when(bookingRepository.existsPastActiveBookingByCustomer_IdAndRestaurant_Id(customer.getId(), restaurant.getId()))
                .thenReturn(true);
        when(orderRepository.existsByBuyer_IdAndDish_Id(customer.getId(), reviewRequestDTO.getDishId()))
                .thenReturn(true);
        when(reviewRepository.save(review)).thenReturn(review);
        when(reviewMapper.reviewToReviewResponseDTO(review)).thenReturn(TestDataUtil.createTestReviewResponseDTO());

        // Act
        ReviewResponseDTO responseDTO = reviewService.save(reviewRequestDTO);

        // Assert
        assertNotNull(responseDTO);
        verify(reviewRepository, times(1)).save(review);
    }

    @Test
    void saveWhenDishNotFoundThrowsEntityNotFoundException() {
        // Arrange
        CustomerUser customer = TestDataUtil.createTestCustomerUser();
        ReviewRequestDTO reviewRequestDTO = TestDataUtil.createTestReviewRequestDTO();
        Review review = TestDataUtil.createTestReview();
        Restaurant restaurant = TestDataUtil.createTestRestaurant();
        mockSecurityContext(customer);

        when(reviewMapper.reviewRequestDTOToReview(reviewRequestDTO)).thenReturn(review);
        when(restaurantRepository.findByIdAndApproved(reviewRequestDTO.getRestaurantId(), true))
                .thenReturn(Optional.of(restaurant));
        when(dishRepository.findById(reviewRequestDTO.getDishId())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> reviewService.save(reviewRequestDTO));
    }

    @Test
    void saveWhenPastActiveBookingDoesNotExistThrowsReviewNotAllowedException() {
        // Arrange
        CustomerUser customer = TestDataUtil.createTestCustomerUser();
        ReviewRequestDTO reviewRequestDTO = TestDataUtil.createTestReviewRequestDTO();
        Review review = TestDataUtil.createTestReview();
        Restaurant restaurant = TestDataUtil.createTestRestaurant();
        mockSecurityContext(customer);

        when(reviewMapper.reviewRequestDTOToReview(reviewRequestDTO)).thenReturn(review);
        when(restaurantRepository.findByIdAndApproved(reviewRequestDTO.getRestaurantId(), true))
                .thenReturn(Optional.of(restaurant));
        when(dishRepository.findById(reviewRequestDTO.getDishId()))
                .thenReturn(Optional.of(TestDataUtil.createTestDish()));
        when(bookingRepository.existsPastActiveBookingByCustomer_IdAndRestaurant_Id(customer.getId(), restaurant.getId()))
                .thenReturn(false);

        // Act & Assert
        assertThrows(ReviewNotAllowedException.class, () -> reviewService.save(reviewRequestDTO));
    }

    @Test
    void saveWhenOrderDoesNotExistThrowsReviewNotAllowedException() {
        // Arrange
        CustomerUser customer = TestDataUtil.createTestCustomerUser();
        ReviewRequestDTO reviewRequestDTO = TestDataUtil.createTestReviewRequestDTO();
        Review review = TestDataUtil.createTestReview();
        Restaurant restaurant = TestDataUtil.createTestRestaurant();
        mockSecurityContext(customer);

        when(reviewMapper.reviewRequestDTOToReview(reviewRequestDTO)).thenReturn(review);
        when(restaurantRepository.findByIdAndApproved(reviewRequestDTO.getRestaurantId(), true))
                .thenReturn(Optional.of(restaurant));
        when(dishRepository.findById(reviewRequestDTO.getDishId()))
                .thenReturn(Optional.of(TestDataUtil.createTestDish()));
        when(bookingRepository.existsPastActiveBookingByCustomer_IdAndRestaurant_Id(customer.getId(), restaurant.getId()))
                .thenReturn(true);
        when(orderRepository.existsByBuyer_IdAndDish_Id(customer.getId(), reviewRequestDTO.getDishId()))
                .thenReturn(false);

        // Act & Assert
        assertThrows(ReviewNotAllowedException.class, () -> reviewService.save(reviewRequestDTO));
    }

    @Test
    void saveWhenSaveFailsThrowsEntityCreationException() {
        // Arrange
        CustomerUser customer = TestDataUtil.createTestCustomerUser();
        ReviewRequestDTO reviewRequestDTO = TestDataUtil.createTestReviewRequestDTO();
        Review review = TestDataUtil.createTestReview();
        Restaurant restaurant = TestDataUtil.createTestRestaurant();
        mockSecurityContext(customer);

        when(reviewMapper.reviewRequestDTOToReview(reviewRequestDTO)).thenReturn(review);
        when(restaurantRepository.findByIdAndApproved(reviewRequestDTO.getRestaurantId(), true))
                .thenReturn(Optional.of(restaurant));
        when(dishRepository.findById(reviewRequestDTO.getDishId()))
                .thenReturn(Optional.of(TestDataUtil.createTestDish()));
        when(bookingRepository.existsPastActiveBookingByCustomer_IdAndRestaurant_Id(customer.getId(), restaurant.getId()))
                .thenReturn(true);
        when(orderRepository.existsByBuyer_IdAndDish_Id(customer.getId(), reviewRequestDTO.getDishId()))
                .thenReturn(true);
        when(reviewRepository.save(review)).thenThrow(new RuntimeException());

        // Act & Assert
        assertThrows(EntityCreationException.class, () -> reviewService.save(reviewRequestDTO));
    }

    @Test
    void findAllWhenReviewsExistReturnsReviewResponseDTOs() {
        // Arrange
        List<Review> reviews = List.of(TestDataUtil.createTestReview());

        when(reviewRepository.findAllByOrderByCreatedAtDesc()).thenReturn(reviews);
        when(reviewMapper.reviewsToReviewResponseDTOs(reviews))
                .thenReturn(List.of(TestDataUtil.createTestReviewResponseDTO()));

        // Act
        List<ReviewResponseDTO> responseDTOs = reviewService.findAll();

        // Assert
        assertNotNull(responseDTOs);
        assertEquals(1, responseDTOs.size());
    }

    @Test
    void findByIdWhenUserIsCustomerReturnsReviewResponseDTO() {
        // Arrange
        CustomerUser customer = TestDataUtil.createTestCustomerUser();
        Review review = TestDataUtil.createTestReview();
        mockSecurityContext(customer);

        when(reviewRepository.findById(review.getId())).thenReturn(Optional.of(review));
        when(reviewMapper.reviewToReviewResponseDTO(review)).thenReturn(TestDataUtil.createTestReviewResponseDTO());

        // Act
        ReviewResponseDTO responseDTO = reviewService.findById(review.getId());

        // Assert
        assertNotNull(responseDTO);
        verify(reviewRepository, times(1)).findById(review.getId());
    }

    @Test
    void findByIdWhenUserIsRestaurateurReturnsReviewResponseDTO() {
        // Arrange
        RestaurateurUser restaurateur = TestDataUtil.createTestRestaurateurUser();
        Review review = TestDataUtil.createTestReview();
        restaurateur.setId(2L);
        review.getRestaurant().setRestaurateur(restaurateur);
        mockSecurityContext(restaurateur);

        when(reviewRepository.findById(review.getId())).thenReturn(Optional.of(review));
        when(reviewMapper.reviewToReviewResponseDTO(review)).thenReturn(TestDataUtil.createTestReviewResponseDTO());

        // Act
        ReviewResponseDTO responseDTO = reviewService.findById(review.getId());

        // Assert
        assertNotNull(responseDTO);
        verify(reviewRepository, times(1)).findById(review.getId());
    }

    @Test
    void findByIdWhenUserIsCookAndReviewIsReturnsReviewResponseDTO() {
        // Arrange
        CookUser cook = TestDataUtil.createTestCookUser();
        Review review = TestDataUtil.createTestReview();
        cook.setId(2L);
        review.getRestaurant().setEmployees(List.of(cook));
        mockSecurityContext(cook);

        when(reviewRepository.findById(review.getId())).thenReturn(Optional.of(review));
        when(reviewMapper.reviewToReviewResponseDTO(review)).thenReturn(TestDataUtil.createTestReviewResponseDTO());

        // Act
        ReviewResponseDTO responseDTO = reviewService.findById(review.getId());

        // Assert
        assertNotNull(responseDTO);
        verify(reviewRepository, times(1)).findById(review.getId());
    }

    @Test
    void findByIdWhenReviewNotFoundThrowsEntityNotFoundException() {
        // Arrange
        when(reviewRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> reviewService.findById(1L));
    }

    @Test
    void findByIdWhenUserIsAdminReturnsReviewResponseDTO() {
        // Arrange
        AdminUser admin = TestDataUtil.createTestAdminUser();
        Review review = TestDataUtil.createTestReview();
        mockSecurityContext(admin);

        when(reviewRepository.findById(review.getId())).thenReturn(Optional.of(review));
        when(reviewMapper.reviewToReviewResponseDTO(review)).thenReturn(TestDataUtil.createTestReviewResponseDTO());

        // Act
        ReviewResponseDTO responseDTO = reviewService.findById(review.getId());

        // Assert
        assertNotNull(responseDTO);
        verify(reviewRepository, times(1)).findById(review.getId());
    }

    @Test
    void findByIdWhenCustomerIsNotReviewOwnerThrowsForbiddenReviewAccessException() {
        // Arrange
        CustomerUser customer = TestDataUtil.createTestCustomerUser();
        CustomerUser anotherCustomer = TestDataUtil.createTestCustomerUser();
        Review review = TestDataUtil.createTestReview();
        customer.setId(2L);
        anotherCustomer.setId(3L);
        review.setCustomer(anotherCustomer);

        mockSecurityContext(customer);

        when(reviewRepository.findById(review.getId())).thenReturn(Optional.of(review));

        // Act & Assert
        assertThrows(ForbiddenReviewAccessException.class, () -> reviewService.findById(review.getId()));
    }

    @Test
    void findByIdWhenRestaurateurIsNotRestaurantOwnerThrowsForbiddenReviewAccessException() {
        // Arrange
        RestaurateurUser restaurateur = TestDataUtil.createTestRestaurateurUser();
        Review review = TestDataUtil.createTestReview();
        RestaurateurUser anotherRestaurateur = TestDataUtil.createTestRestaurateurUser();
        restaurateur.setId(2L);
        anotherRestaurateur.setId(3L);
        review.getRestaurant().setRestaurateur(anotherRestaurateur);

        mockSecurityContext(restaurateur);

        when(reviewRepository.findById(review.getId())).thenReturn(Optional.of(review));

        // Act & Assert
        assertThrows(ForbiddenReviewAccessException.class, () -> reviewService.findById(review.getId()));
    }

    @Test
    void findByIdWhenCookIsNotRestaurantEmployeeThrowsForbiddenReviewAccessException() {
        // Arrange
        CookUser cook = TestDataUtil.createTestCookUser();
        Review review = TestDataUtil.createTestReview();
        CookUser anotherCook = TestDataUtil.createTestCookUser();
        cook.setId(2L);
        anotherCook.setId(3L);
        review.getRestaurant().setEmployees(List.of(anotherCook));

        mockSecurityContext(cook);

        when(reviewRepository.findById(review.getId())).thenReturn(Optional.of(review));

        // Act & Assert
        assertThrows(ForbiddenReviewAccessException.class, () -> reviewService.findById(review.getId()));
    }

    @Test
    void findAllByCustomerWhenReviewsExistReturnsReviewResponseDTOs() {
        // Arrange
        CustomerUser customer = TestDataUtil.createTestCustomerUser();
        List<Review> reviews = List.of(TestDataUtil.createTestReview());

        when(reviewRepository.findAllByCustomer_IdOrderByCreatedAtDesc(customer.getId())).thenReturn(reviews);
        when(reviewMapper.reviewsToReviewResponseDTOs(reviews))
                .thenReturn(List.of(TestDataUtil.createTestReviewResponseDTO()));

        // Act
        List<ReviewResponseDTO> responseDTOs = reviewService.findAllByCustomer(customer.getId());

        // Assert
        assertNotNull(responseDTOs);
        assertEquals(1, responseDTOs.size());
    }

    @Test
    void findAllByRestaurantWhenReviewsExistReturnsReviewResponseDTOs() {
        // Arrange
        Restaurant restaurant = TestDataUtil.createTestRestaurant();
        List<Review> reviews = List.of(TestDataUtil.createTestReview());

        when(reviewRepository.findAllByRestaurant_IdOrderByCreatedAtDesc(restaurant.getId())).thenReturn(reviews);
        when(reviewMapper.reviewsToReviewResponseDTOs(reviews))
                .thenReturn(List.of(TestDataUtil.createTestReviewResponseDTO()));

        // Act
        List<ReviewResponseDTO> responseDTOs = reviewService.findAllByRestaurant(restaurant.getId());

        // Assert
        assertNotNull(responseDTOs);
        assertEquals(1, responseDTOs.size());
    }

    @Test
    void findAllByDishWhenReviewsExistReturnsReviewResponseDTOs() {
        // Arrange
        Dish dish = TestDataUtil.createTestDish();
        List<Review> reviews = List.of(TestDataUtil.createTestReview());

        when(reviewRepository.findAllByDish_IdOrderByCreatedAtDesc(dish.getId())).thenReturn(reviews);
        when(reviewMapper.reviewsToReviewResponseDTOs(reviews))
                .thenReturn(List.of(TestDataUtil.createTestReviewResponseDTO()));

        // Act
        List<ReviewResponseDTO> responseDTOs = reviewService.findAllByDish(dish.getId());

        // Assert
        assertNotNull(responseDTOs);
        assertEquals(1, responseDTOs.size());
    }

    @Test
    void removeWhenReviewExistsAndUserIsOwnerReturnsTrue() {
        // Arrange
        CustomerUser customer = TestDataUtil.createTestCustomerUser();
        Review review = TestDataUtil.createTestReview();
        review.setCustomer(customer);

        mockSecurityContext(customer);

        when(reviewRepository.findById(review.getId())).thenReturn(Optional.of(review));

        // Act
        boolean result = reviewService.remove(review.getId());

        // Assert
        assertTrue(result);
        verify(reviewRepository, times(1)).save(review);
    }

    @Test
    void removeWhenUserIsAdminReturnsTrue() {
        // Arrange
        AdminUser admin = TestDataUtil.createTestAdminUser();
        Review review = TestDataUtil.createTestReview();

        mockSecurityContext(admin);

        when(reviewRepository.findById(review.getId())).thenReturn(Optional.of(review));

        // Act
        boolean result = reviewService.remove(review.getId());

        // Assert
        assertTrue(result);
        verify(reviewRepository, times(1)).save(review);
    }

    @Test
    void removeWhenUserIsNotOwnerThrowsForbiddenReviewAccessException() {
        // Arrange
        CustomerUser customer = TestDataUtil.createTestCustomerUser();
        CustomerUser anotherCustomer = TestDataUtil.createTestCustomerUser();
        anotherCustomer.setId(2L);
        Review review = TestDataUtil.createTestReview();
        review.setCustomer(anotherCustomer);

        mockSecurityContext(customer);

        when(reviewRepository.findById(review.getId())).thenReturn(Optional.of(review));

        // Act & Assert
        assertThrows(ForbiddenReviewAccessException.class, () -> reviewService.remove(review.getId()));
    }

    @Test
    void removeWhenSaveFailsThrowsEntityDeletionException() {
        // Arrange
        CustomerUser customer = TestDataUtil.createTestCustomerUser();
        Review review = TestDataUtil.createTestReview();
        review.setCustomer(customer);

        mockSecurityContext(customer);

        when(reviewRepository.findById(review.getId())).thenReturn(Optional.of(review));
        doThrow(new RuntimeException()).when(reviewRepository).save(review);

        // Act & Assert
        assertThrows(EntityDeletionException.class, () -> reviewService.remove(review.getId()));
    }
}