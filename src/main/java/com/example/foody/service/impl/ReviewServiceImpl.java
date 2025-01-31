package com.example.foody.service.impl;

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
import com.example.foody.model.user.CustomerUser;
import com.example.foody.model.user.User;
import com.example.foody.observer.listener.impl.RestaurantStaffNewReviewEventListener;
import com.example.foody.observer.manager.EventManager;
import com.example.foody.repository.*;
import com.example.foody.service.EmailService;
import com.example.foody.service.ReviewService;
import com.example.foody.utils.UserRoleUtils;
import com.example.foody.utils.enums.EventType;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of the ReviewService interface.
 * <p>
 * Provides methods to create, read, update, and delete {@link Review} objects.
 */
@Service
@Transactional(rollbackOn = Exception.class)
@AllArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final RestaurantRepository restaurantRepository;
    private final DishRepository dishRepository;
    private final BookingRepository bookingRepository;
    private final OrderRepository orderRepository;
    private final ReviewMapper reviewMapper;
    private final EmailService emailService;
    private final EventManager eventManager;

    /**
     * {@inheritDoc}
     * <p>
     * This method persists a new {@link Review} entity to the database.
     *
     * @param reviewDTO the review data transfer object
     * @return the saved review response data transfer object
     * @throws EntityCreationException if there is an error during review creation
     */
    @Override
    public ReviewResponseDTO save(ReviewRequestDTO reviewDTO) {
        CustomerUser principal = (CustomerUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Review review = reviewMapper.reviewRequestDTOToReview(reviewDTO);
        Restaurant restaurant = restaurantRepository
                .findByIdAndApproved(reviewDTO.getRestaurantId(), true)
                .orElseThrow(() -> new EntityNotFoundException("restaurant", "id", reviewDTO.getRestaurantId()));

        Optional.ofNullable(reviewDTO.getDishId()).ifPresent(dishId -> setReviewDishOrThrow(review, dishId));

        review.setCustomer(principal);
        review.setRestaurant(restaurant);

        checkReviewCreationOrThrow(principal, review);

        try {
            reviewRepository.save(review);
        } catch (Exception e) {
            throw new EntityCreationException("review");
        }

        notifyNewReviewListeners(review);

        return reviewMapper.reviewToReviewResponseDTO(review);
    }

    /**
     * {@inheritDoc}
     * <p>
     * This method retrieves all {@link Review} entities from the database.
     *
     * @return a list of review response data transfer objects
     */
    @Override
    public List<ReviewResponseDTO> findAll() {
        List<Review> reviews = reviewRepository.findAllByOrderByCreatedAtDesc();
        return reviewMapper.reviewsToReviewResponseDTOs(reviews);
    }

    /**
     * {@inheritDoc}
     * <p>
     * This method retrieves a {@link Review} entity by its ID.
     *
     * @param id the review ID
     * @return the review response data transfer object
     * @throws EntityNotFoundException if the review is not found
     */
    @Override
    public ReviewResponseDTO findById(long id) {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Review review = reviewRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("review", "id", id));

        checkReviewAccessOrThrow(principal, review);

        return reviewMapper.reviewToReviewResponseDTO(review);
    }

    /**
     * {@inheritDoc}
     * <p>
     * This method retrieves all {@link Review} entities by customer ID.
     *
     * @param customerId the customer ID
     * @return a list of review response data transfer objects
     */
    @Override
    public List<ReviewResponseDTO> findAllByCustomer(long customerId) {
        List<Review> reviews = reviewRepository
                .findAllByCustomer_IdOrderByCreatedAtDesc(customerId);
        return reviewMapper.reviewsToReviewResponseDTOs(reviews);
    }

    /**
     * {@inheritDoc}
     * <p>
     * This method retrieves all {@link Review} entities by restaurant ID.
     *
     * @param restaurantId the restaurant ID
     * @return a list of review response data transfer objects
     */
    @Override
    public List<ReviewResponseDTO> findAllByRestaurant(long restaurantId) {
        List<Review> reviews = reviewRepository
                .findAllByRestaurant_IdOrderByCreatedAtDesc(restaurantId);
        return reviewMapper.reviewsToReviewResponseDTOs(reviews);
    }

    /**
     * {@inheritDoc}
     * <p>
     * This method retrieves all {@link Review} entities by dish ID.
     *
     * @param dishId the dish ID
     * @return a list of review response data transfer objects
     */
    @Override
    public List<ReviewResponseDTO> findAllByDish(long dishId) {
        List<Review> reviews = reviewRepository
                .findAllByDish_IdOrderByCreatedAtDesc(dishId);
        return reviewMapper.reviewsToReviewResponseDTOs(reviews);
    }

    /**
     * {@inheritDoc}
     * <p>
     * This method removes a {@link Review} entity by its ID.
     *
     * @param id the review ID
     * @return true if the review was successfully removed, false otherwise
     * @throws EntityNotFoundException if the review is not found
     * @throws EntityDeletionException if there is an error during review deletion
     */
    @Override
    public boolean remove(long id) {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Review review = reviewRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("review", "id", id));
        review.delete();

        checkReviewDeletionOrThrow(principal, review);

        try {
            reviewRepository.save(review);
        } catch (Exception e) {
            throw new EntityDeletionException("review", "id", id);
        }

        return true;
    }

    /**
     * Sets the dish for a review or throws an exception if the dish is not found.
     *
     * @param review the review
     * @param dishId the dish ID
     * @throws EntityNotFoundException if the dish is not found
     */
    private void setReviewDishOrThrow(Review review, long dishId) {
        Dish dish = dishRepository
                .findById(dishId)
                .orElseThrow(() -> new EntityNotFoundException("dish", "id", dishId));
        review.setDish(dish);
    }

    /**
     * Checks if the review can be created or throws an exception if not.
     *
     * @param user   the user
     * @param review the review
     * @throws ReviewNotAllowedException if the review is not allowed
     */
    private void checkReviewCreationOrThrow(User user, Review review) {
        checkPastActiveBookingOrThrow(user, review);
        Optional.ofNullable(review.getDish()).ifPresent(dish -> checkDishReviewCreationOrThrow(user, review));
    }

    /**
     * Checks if there is a past active booking for the review or throws an exception if not.
     *
     * @param user   the user
     * @param review the review
     * @throws ReviewNotAllowedException if there is no past active booking
     */
    private void checkPastActiveBookingOrThrow(User user, Review review) {
        if (bookingRepository.existsPastActiveBookingByCustomer_IdAndRestaurant_Id(
                user.getId(),
                review.getRestaurant().getId()
        )) return;

        throw new ReviewNotAllowedException(
                "restaurant",
                review.getRestaurant().getId(),
                "there are no past active bookings for the restaurant"
        );
    }

    /**
     * Checks if the dish review can be created or throws an exception if not.
     *
     * @param user   the user
     * @param review the review
     * @throws ReviewNotAllowedException if the dish review is not allowed
     */
    private void checkDishReviewCreationOrThrow(User user, Review review) {
        if (orderRepository.existsByBuyer_IdAndDish_Id(user.getId(), review.getDish().getId())) return;

        throw new ReviewNotAllowedException("dish", review.getDish().getId(), "there are no orders for the dish");
    }

    /**
     * Checks if the user has access to the review or throws an exception if not.
     *
     * @param user   the user
     * @param review the review
     * @throws ForbiddenReviewAccessException if the user does not have access to the review
     */
    private void checkReviewAccessOrThrow(User user, Review review) {
        if (!UserRoleUtils.isCustomer(user) && !UserRoleUtils.isRestaurateur(user) && !UserRoleUtils.isEmployee(user))
            return;
        if (review.getCustomer().getId() == user.getId()) return;
        if (review.getRestaurant().getRestaurateur().getId() == user.getId()) return;
        if (review.getRestaurant().getEmployees().stream().anyMatch(employee -> employee.getId() == user.getId()))
            return;

        throw new ForbiddenReviewAccessException();
    }

    /**
     * Checks if the review can be deleted or throws an exception if not.
     *
     * @param user   the user
     * @param review the review
     * @throws ForbiddenReviewAccessException if the review cannot be deleted
     */
    private void checkReviewDeletionOrThrow(User user, Review review) {
        if (!UserRoleUtils.isCustomer(user)) return;
        if (review.getCustomer().getId() == user.getId()) return;

        throw new ForbiddenReviewAccessException();
    }

    /**
     * Notifies listeners that a new review has been created.
     *
     * @param review the review
     */
    private void notifyNewReviewListeners(Review review) {
        subscribeNewReviewListeners(review);
        eventManager.notifyListeners(EventType.NEW_REVIEW, review);
    }

    /**
     * Subscribes listeners for new review events.
     *
     * @param review the review
     */
    private void subscribeNewReviewListeners(Review review) {
        subscribeRestaurateurUserNewReviewListener(review);
        subscribeEmployeesNewReviewListeners(review);
    }

    /**
     * Subscribes the restaurateur user as a listener for new review events.
     *
     * @param review the review
     */
    private void subscribeRestaurateurUserNewReviewListener(Review review) {
        eventManager.subscribe(
                EventType.NEW_REVIEW,
                new RestaurantStaffNewReviewEventListener(emailService, review.getRestaurant().getRestaurateur())
        );
    }

    /**
     * Subscribes the employees as listeners for new review events.
     *
     * @param review the review
     */
    private void subscribeEmployeesNewReviewListeners(Review review) {
        review.getRestaurant().getEmployees().forEach(employee ->
                eventManager.subscribe(
                        EventType.NEW_REVIEW,
                        new RestaurantStaffNewReviewEventListener(emailService, employee)
                )
        );
    }
}