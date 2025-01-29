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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(rollbackOn = Exception.class)
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final RestaurantRepository restaurantRepository;
    private final DishRepository dishRepository;
    private final BookingRepository bookingRepository;
    private final OrderRepository orderRepository;
    private final ReviewMapper reviewMapper;
    private final EmailService emailService;
    private final EventManager eventManager;

    public ReviewServiceImpl(ReviewRepository reviewRepository, RestaurantRepository restaurantRepository, DishRepository dishRepository, BookingRepository bookingRepository, OrderRepository orderRepository, ReviewMapper reviewMapper, EmailService emailService, EventManager eventManager) {
        this.reviewRepository = reviewRepository;
        this.restaurantRepository = restaurantRepository;
        this.dishRepository = dishRepository;
        this.bookingRepository = bookingRepository;
        this.orderRepository = orderRepository;
        this.reviewMapper = reviewMapper;
        this.emailService = emailService;
        this.eventManager = eventManager;
    }

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

    @Override
    public List<ReviewResponseDTO> findAll() {
        List<Review> reviews = reviewRepository.findAllByOrderByCreatedAtDesc();
        return reviewMapper.reviewsToReviewResponseDTOs(reviews);
    }

    @Override
    public ReviewResponseDTO findById(long id) {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Review review = reviewRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("review", "id", id));

        checkReviewAccessOrThrow(principal, review);

        return reviewMapper.reviewToReviewResponseDTO(review);
    }

    @Override
    public List<ReviewResponseDTO> findAllByCustomer(long customerId) {
        List<Review> reviews = reviewRepository
                .findAllByCustomer_IdOrderByCreatedAtDesc(customerId);
        return reviewMapper.reviewsToReviewResponseDTOs(reviews);
    }

    @Override
    public List<ReviewResponseDTO> findAllByRestaurant(long restaurantId) {
        List<Review> reviews = reviewRepository
                .findAllByRestaurant_IdOrderByCreatedAtDesc(restaurantId);
        return reviewMapper.reviewsToReviewResponseDTOs(reviews);
    }

    @Override
    public List<ReviewResponseDTO> findAllByDish(long dishId) {
        List<Review> reviews = reviewRepository
                .findAllByDish_IdOrderByCreatedAtDesc(dishId);
        return reviewMapper.reviewsToReviewResponseDTOs(reviews);
    }

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

    private void setReviewDishOrThrow(Review review, long dishId) {
        Dish dish = dishRepository
                .findById(dishId)
                .orElseThrow(() -> new EntityNotFoundException("dish", "id", dishId));
        review.setDish(dish);
    }

    /*
        (Restaurant's review) Check if there is a booking with:
        - in the past
        - customer_id equal to that of the review
        - restaurant_id equal to that of the review
        (Dish's review) Check if there is a order with:
        - buyer_id equal to that of the review
        - dish_id equal to that of the review
     */
    private void checkReviewCreationOrThrow(User user, Review review) {
        checkPastActiveBookingOrThrow(user, review);
        Optional.ofNullable(review.getDish()).ifPresent(dish -> checkDishReviewCreationOrThrow(user, review));
    }

    private void checkPastActiveBookingOrThrow(User user, Review review) {
        if (bookingRepository.existsPastActiveBookingByCustomer_IdAndRestaurant_Id(user.getId(), review.getRestaurant().getId()))
            return;

        throw new ReviewNotAllowedException("restaurant", review.getRestaurant().getId(), "there are no past active bookings for the restaurant");
    }

    private void checkDishReviewCreationOrThrow(User user, Review review) {
        if (orderRepository.existsByBuyer_IdAndDish_Id(user.getId(), review.getDish().getId())) return;

        throw new ReviewNotAllowedException("dish", review.getDish().getId(), "there are no orders for the dish");
    }

    private void checkReviewAccessOrThrow(User user, Review review) {
        if (!UserRoleUtils.isCustomer(user) && !UserRoleUtils.isRestaurateur(user) && !UserRoleUtils.isEmployee(user))
            return;
        if (review.getCustomer().getId() == user.getId()) return;
        if (review.getRestaurant().getRestaurateur().getId() == user.getId()) return;
        if (review.getRestaurant().getEmployees().stream().anyMatch(employee -> employee.getId() == user.getId()))
            return;

        throw new ForbiddenReviewAccessException();
    }

    private void checkReviewDeletionOrThrow(User user, Review review) {
        if (!UserRoleUtils.isCustomer(user)) return;
        if (review.getCustomer().getId() == user.getId()) return;

        throw new ForbiddenReviewAccessException();
    }

    private void notifyNewReviewListeners(Review review) {
        subscribeNewReviewListeners(review);
        eventManager.notifyListeners(EventType.NEW_REVIEW, review);
    }

    private void subscribeNewReviewListeners(Review review) {
        subscribeRestaurateurUserNewReviewListener(review);
        subscribeEmployeesNewReviewListeners(review);
    }

    private void subscribeRestaurateurUserNewReviewListener(Review review) {
        eventManager.subscribe(
                EventType.NEW_REVIEW,
                new RestaurantStaffNewReviewEventListener(emailService, review.getRestaurant().getRestaurateur())
        );
    }

    private void subscribeEmployeesNewReviewListeners(Review review) {
        review.getRestaurant().getEmployees().forEach(employee ->
                eventManager.subscribe(
                        EventType.NEW_REVIEW,
                        new RestaurantStaffNewReviewEventListener(emailService, employee)
                )
        );
    }
}