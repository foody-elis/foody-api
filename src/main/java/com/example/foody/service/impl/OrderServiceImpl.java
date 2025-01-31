package com.example.foody.service.impl;

import com.example.foody.dto.request.OrderDishRequestDTO;
import com.example.foody.dto.request.OrderRequestDTO;
import com.example.foody.dto.response.OrderResponseDTO;
import com.example.foody.exceptions.entity.EntityCreationException;
import com.example.foody.exceptions.entity.EntityDeletionException;
import com.example.foody.exceptions.entity.EntityEditException;
import com.example.foody.exceptions.entity.EntityNotFoundException;
import com.example.foody.exceptions.order.ForbiddenOrderAccessException;
import com.example.foody.exceptions.order.InvalidOrderStateException;
import com.example.foody.exceptions.order.OrderNotAllowedException;
import com.example.foody.exceptions.restaurant.ForbiddenRestaurantAccessException;
import com.example.foody.mapper.OrderMapper;
import com.example.foody.model.Dish;
import com.example.foody.model.Order;
import com.example.foody.model.Restaurant;
import com.example.foody.model.order_dish.OrderDish;
import com.example.foody.model.user.BuyerUser;
import com.example.foody.model.user.CookUser;
import com.example.foody.model.user.User;
import com.example.foody.observer.listener.impl.CookUserOrderCreatedEventListener;
import com.example.foody.observer.listener.impl.CustomerUserOrderCompletedEventListener;
import com.example.foody.observer.manager.EventManager;
import com.example.foody.repository.*;
import com.example.foody.service.EmailService;
import com.example.foody.service.OrderService;
import com.example.foody.state.order.OrderState;
import com.example.foody.state.order.impl.CreatedState;
import com.example.foody.state.order.impl.PaidState;
import com.example.foody.utils.UserRoleUtils;
import com.example.foody.utils.enums.*;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Implementation of the {@link OrderService} interface.
 * <p>
 * Provides methods to create, update, and delete {@link Order} objects.
 */
@Service
@Transactional(rollbackOn = Exception.class)
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final RestaurantRepository restaurantRepository;
    private final BookingRepository bookingRepository;
    private final DishRepository dishRepository;
    private final OrderDishRepository orderDishRepository;
    private final OrderMapper orderMapper;
    private final EmailService emailService;
    private final EventManager eventManager;
    private final SimpMessagingTemplate messagingTemplate;

    /**
     * {@inheritDoc}
     * <p>
     * This method persists a new {@link Order} entity to the database.
     *
     * @param orderDTO the order request data transfer object
     * @return the saved order response data transfer object
     * @throws EntityCreationException if there is an error during the creation of the order
     * @throws EntityNotFoundException if the restaurant is not found
     */
    @Override
    public OrderResponseDTO save(OrderRequestDTO orderDTO) {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Order order = orderMapper.orderRequestDTOToOrder(orderDTO);
        Restaurant restaurant = restaurantRepository
                .findByIdAndApproved(orderDTO.getRestaurantId(), true)
                .orElseThrow(() -> new EntityNotFoundException("restaurant", "id", orderDTO.getRestaurantId()));

        order.setBuyer(new BuyerUser(principal.getId(), new ArrayList<>()));
        order.setRestaurant(restaurant);
        order.setState(getInitialOrderState(principal));

        checkOrderCreationOrThrow(principal, order);

        try {
            order = orderRepository.save(order);
        } catch (Exception e) {
            throw new EntityCreationException("order");
        }

        order.setOrderDishes(addDishesToOrder(order, orderDTO.getOrderDishes()));
        order.getBuyer().setUser(principal);

        notifyOrderCreatedListeners(order);

        OrderResponseDTO orderResponseDTO = orderMapper.orderToOrderResponseDTO(order);

        if (order.getStatus() == OrderStatus.PAID) {
            messagingTemplate.convertAndSend(
                    WebSocketTopics.TOPIC_ORDERS_PAYED.getName() + order.getRestaurant().getId(),
                    orderResponseDTO
            );
        }

        return orderResponseDTO;
    }

    /**
     * {@inheritDoc}
     * <p>
     * This method retrieves all {@link Order} entities from the database.
     *
     * @return the list of order response data transfer objects
     */
    @Override
    public List<OrderResponseDTO> findAll() {
        List<Order> orders = orderRepository.findAll();
        return orderMapper.ordersToOrderResponseDTOs(orders);
    }

    /**
     * {@inheritDoc}
     * <p>
     * This method retrieves a {@link Order} entity by its ID.
     *
     * @param id the ID of the order to retrieve
     * @return the order response data transfer object
     * @throws EntityNotFoundException if the order with the specified ID is not found
     */
    @Override
    public OrderResponseDTO findById(long id) {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Order order = orderRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("order", "id", id));

        checkOrderAccessOrThrow(principal, order);

        return orderMapper.orderToOrderResponseDTO(order);
    }

    /**
     * {@inheritDoc}
     * <p>
     * This method retrieves all {@link Order} entities for a specific buyer.
     *
     * @param buyerId the ID of the buyer
     * @return the list of order response data transfer objects
     */
    @Override
    public List<OrderResponseDTO> findAllByBuyer(long buyerId) {
        List<Order> orders = orderRepository
                .findAllByBuyer_IdOrderByCreatedAtDesc(buyerId);
        return orderMapper.ordersToOrderResponseDTOs(orders);
    }

    /**
     * {@inheritDoc}
     * <p>
     * This method retrieves all {@link Order} entities for a specific restaurant.
     *
     * @param restaurantId the ID of the restaurant
     * @return the list of order response data transfer objects
     */
    @Override
    public List<OrderResponseDTO> findAllByRestaurant(long restaurantId) {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Restaurant restaurant = restaurantRepository
                .findByIdAndApproved(restaurantId, true)
                .orElseThrow(() -> new EntityNotFoundException("restaurant", "id", restaurantId));

        checkRestaurantAccessOrThrow(principal, restaurant);

        List<Order> orders = orderRepository
                .findAllByRestaurant_IdOrderByCreatedAtDesc(restaurantId);

        return orderMapper.ordersToOrderResponseDTOs(orders);
    }

    /**
     * {@inheritDoc}
     * <p>
     * This method retrieves all {@link Order} entities for a specific restaurant that are in progress.
     *
     * @param restaurantId the ID of the restaurant
     * @return the list of order response data transfer objects
     */
    @Override
    public List<OrderResponseDTO> findAllByRestaurantAndInProgress(long restaurantId) {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Restaurant restaurant = restaurantRepository
                .findByIdAndApproved(restaurantId, true)
                .orElseThrow(() -> new EntityNotFoundException("restaurant", "id", restaurantId));

        checkRestaurantAccessOrThrow(principal, restaurant);

        List<Order> orders = orderRepository.findAllByRestaurant_IdAndStatusInOrderByCreatedAtDesc(
                restaurantId,
                List.of(OrderStatus.PAID.name(), OrderStatus.PREPARING.name())
        );

        return orderMapper.ordersToOrderResponseDTOs(orders);
    }

    /**
     * {@inheritDoc}
     * <p>
     * This method updates the status of an {@link Order} entity to "paid".
     *
     * @param id the ID of the order to update
     * @return the updated order response data transfer object
     * @throws EntityNotFoundException    if the order with the specified ID is not found
     * @throws EntityEditException        if there is an error during the update of the order
     * @throws InvalidOrderStateException if the order is in an invalid state for this operation
     */
    @Override
    public OrderResponseDTO payById(long id) {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Order order = orderRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("order", "id", id));

        checkPayAccessOrThrow(principal, order);

        try {
            order.pay();
            order = orderRepository.save(order);
        } catch (IllegalStateException e) {
            throw new InvalidOrderStateException(e.getMessage());
        } catch (Exception e) {
            throw new EntityEditException("order", "id", id);
        }

        sendPaymentReceivedEmail(order);

        OrderResponseDTO orderResponseDTO = orderMapper.orderToOrderResponseDTO(order);

        messagingTemplate.convertAndSend(
                WebSocketTopics.TOPIC_ORDERS_PAYED.getName() + order.getRestaurant().getId(),
                orderResponseDTO
        );

        return orderResponseDTO;
    }

    /**
     * {@inheritDoc}
     * <p>
     * This method updates the status of an {@link Order} entity to "preparing".
     *
     * @param id the ID of the order to update
     * @return the updated order response data transfer object
     * @throws EntityNotFoundException    if the order with the specified ID is not found
     * @throws EntityEditException        if there is an error during the update of the order
     * @throws InvalidOrderStateException if the order is in an invalid state for this operation
     */
    @Override
    public OrderResponseDTO prepareById(long id) {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Order order = orderRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("order", "id", id));

        checkPrepareAccessOrThrow(principal, order);

        try {
            order.prepare();
            order = orderRepository.save(order);
        } catch (IllegalStateException e) {
            throw new InvalidOrderStateException(e.getMessage());
        } catch (Exception e) {
            throw new EntityEditException("order", "id", id);
        }

        OrderResponseDTO orderResponseDTO = orderMapper.orderToOrderResponseDTO(order);

        messagingTemplate.convertAndSend(
                WebSocketTopics.TOPIC_ORDERS_PREPARING.getName() + order.getRestaurant().getId(),
                orderResponseDTO
        );

        return orderResponseDTO;
    }

    /**
     * {@inheritDoc}
     * <p>
     * This method updates the status of an {@link Order} entity to "completed".
     *
     * @param id the ID of the order to update
     * @return the updated order response data transfer object
     * @throws EntityNotFoundException    if the order with the specified ID is not found
     * @throws EntityEditException        if there is an error during the update of the order
     * @throws InvalidOrderStateException if the order is in an invalid state for this operation
     */
    @Override
    public OrderResponseDTO completeById(long id) {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Order order = orderRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("order", "id", id));

        checkCompleteAccessOrThrow(principal, order);

        try {
            order.complete();
            order = orderRepository.save(order);
        } catch (IllegalStateException e) {
            throw new InvalidOrderStateException(e.getMessage());
        } catch (Exception e) {
            throw new EntityEditException("order", "id", id);
        }

        notifyOrderCompletedListeners(order);

        OrderResponseDTO orderResponseDTO = orderMapper.orderToOrderResponseDTO(order);

        messagingTemplate.convertAndSend(
                WebSocketTopics.TOPIC_ORDERS_COMPLETED.getName() + order.getRestaurant().getId(),
                orderResponseDTO
        );

        return orderResponseDTO;
    }

    /**
     * {@inheritDoc}
     * <p>
     * This method removes an {@link Order} entity by its ID.
     *
     * @param id the ID of the order to remove
     * @return true if the order was removed, false otherwise
     * @throws EntityNotFoundException if the order with the specified ID is not found
     * @throws EntityDeletionException if there is an error during the deletion of the order
     */
    @Override
    public boolean remove(long id) {
        Order order = orderRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("order", "id", id));
        order.delete();

        try {
            orderRepository.save(order);
        } catch (Exception e) {
            throw new EntityDeletionException("order", "id", id);
        }

        return true;
    }

    /**
     * Adds dishes to the order.
     * <p>
     * This method maps the order dish request DTOs to order dish entities and saves them.
     *
     * @param order         the order to add dishes to
     * @param orderDishDTOs the list of order dish request data transfer objects
     * @return the list of saved order dish entities
     */
    private List<OrderDish> addDishesToOrder(Order order, List<OrderDishRequestDTO> orderDishDTOs) {
        return orderDishDTOs.stream()
                .map(orderDishDTO -> addDishToOrder(order, orderDishDTO))
                .toList();
    }

    /**
     * Adds a dish to the order.
     * <p>
     * This method maps the order dish request DTO to an order dish entity and saves it.
     *
     * @param order        the order to add the dish to
     * @param orderDishDTO the order dish request data transfer object
     * @return the saved order dish entity
     * @throws EntityNotFoundException if the dish with the specified ID is not found
     * @throws EntityCreationException if there is an error during the creation of the order dish
     */
    private OrderDish addDishToOrder(Order order, OrderDishRequestDTO orderDishDTO) {
        Dish dish = dishRepository
                .findById(orderDishDTO.getDishId())
                .orElseThrow(() -> new EntityNotFoundException("dish", "id", orderDishDTO.getDishId()));
        OrderDish orderDish = new OrderDish(order, dish, orderDishDTO.getQuantity());

        try {
            return orderDishRepository.save(orderDish);
        } catch (Exception e) {
            throw new EntityCreationException("order dish");
        }
    }

    /**
     * Retrieves the initial state of the order based on the user role.
     *
     * @param user the user creating the order
     * @return the initial state of the order
     */
    private OrderState getInitialOrderState(User user) {
        return UserRoleUtils.isWaiter(user)
                ? new PaidState()
                : new CreatedState();
    }

    /**
     * Checks if the user has access to create the order.
     * <p>
     * This method throws a {@link ForbiddenOrderAccessException} if the user does not have access to create the order.
     *
     * @param user  the user to check
     * @param order the order to check access for
     * @throws ForbiddenOrderAccessException if access to create the order is forbidden
     */
    private void checkOrderCreationOrThrow(User user, Order order) {
        if (UserRoleUtils.isWaiter(user)) checkIsWaiterOfRestaurant(user, order);
        if (UserRoleUtils.isCustomer(user)) checkActiveBookingOrThrow(order);
        checkDishesBelongToRestaurantOrThrow(order);
    }

    /**
     * Checks if the user is a waiter of the restaurant.
     * <p>
     * This method throws a {@link ForbiddenOrderAccessException} if the user is not a waiter of the restaurant.
     *
     * @param user  the user to check
     * @param order the order to check access for
     * @throws ForbiddenOrderAccessException if the user is not a waiter of the restaurant
     */
    private void checkIsWaiterOfRestaurant(User user, Order order) {
        if (order.getRestaurant().getEmployees().stream().anyMatch(employeeUser ->
                employeeUser.getId() == user.getId()
        )) return;

        throw new ForbiddenOrderAccessException();
    }

    /**
     * Checks if there is an active booking for the order.
     * <p>
     * Conditions for a valid booking:
     * <p>
     * <ul>
     * <li>Must not be in the past.</li>
     * <li>Must have a status of ACTIVE.</li>
     * <li>Must have the same customer ID as the order.</li>
     * <li>Must have the same restaurant ID as the order.</li>
     * <li>Must have a sitting time that meets both conditions:
     * <ul>
     * <li>The weekday must match that of the order.</li>
     * <li>The order time must fall within the sitting time range
     * (<code>startTime <= orderTime <= endTime</code>).</li>
     * </ul>
     * </li>
     * </ul>
     * <p>
     * This method throws an {@link OrderNotAllowedException} if there is no active booking for the order.
     *
     * @param order the order to check
     * @throws OrderNotAllowedException if there is no active booking for the order
     */
    private void checkActiveBookingOrThrow(Order order) {
        if (bookingRepository.existsActiveBookingForOrder(order.getBuyer().getId(), order.getRestaurant().getId()))
            return;

        throw new OrderNotAllowedException(
                order.getRestaurant().getId(),
                "there are no active bookings for the buyer"
        );
    }

    /**
     * Checks if the dishes belong to the restaurant.
     * <p>
     * This method throws an {@link OrderNotAllowedException} if some dishes do not belong to the restaurant.
     *
     * @param order the order to check
     * @throws OrderNotAllowedException if some dishes do not belong to the restaurant
     */
    private void checkDishesBelongToRestaurantOrThrow(Order order) {
        if (order.getOrderDishes().stream().allMatch(orderDish ->
                orderDish.getDish().getRestaurant().getId() == order.getRestaurant().getId()
        )) return;

        throw new OrderNotAllowedException(
                order.getRestaurant().getId(),
                "some dishes do not belong to the restaurant"
        );
    }

    /**
     * Checks if the user has access to the order.
     * <p>
     * This method throws a {@link ForbiddenOrderAccessException} if the user does not have access to the order.
     *
     * @param user  the user to check
     * @param order the order to check access for
     * @throws ForbiddenOrderAccessException if access to the order is forbidden
     */
    private void checkOrderAccessOrThrow(User user, Order order) {
        if (!UserRoleUtils.isRestaurateur(user) && !UserRoleUtils.isBuyer(user)) return;
        if (order.getBuyer().getId() == user.getId()) return;
        if (order.getRestaurant().getRestaurateur().getId() == user.getId()) return;
        if (order.getRestaurant().getEmployees().stream().anyMatch(employeeUser ->
                employeeUser.getId() == user.getId()
        )) return;

        throw new ForbiddenOrderAccessException();
    }

    /**
     * Checks if the user has access to the restaurant.
     * <p>
     * This method throws a {@link ForbiddenRestaurantAccessException} if the user does not have access to the restaurant.
     *
     * @param user       the user to check
     * @param restaurant the restaurant to check access for
     * @throws ForbiddenRestaurantAccessException if access to the restaurant is forbidden
     */
    private void checkRestaurantAccessOrThrow(User user, Restaurant restaurant) {
        if (!UserRoleUtils.isRestaurateur(user) && !UserRoleUtils.isEmployee(user)) return;
        if (restaurant.getRestaurateur().getId() == user.getId()) return;
        if (restaurant.getEmployees().stream().anyMatch(employeeUser -> employeeUser.getId() == user.getId())) return;

        throw new ForbiddenRestaurantAccessException();
    }

    /**
     * Checks if the user has access to pay the order.
     * <p>
     * This method throws a {@link ForbiddenOrderAccessException} if the user does not have access to pay the order.
     *
     * @param user  the user to check
     * @param order the order to check access for
     * @throws ForbiddenOrderAccessException if access to pay the order is forbidden
     */
    private void checkPayAccessOrThrow(User user, Order order) {
        if (!UserRoleUtils.isBuyer(user)) return;
        if (order.getBuyer().getId() == user.getId()) return;

        throw new ForbiddenOrderAccessException();
    }

    /**
     * Sends the payment received email.
     * <p>
     * This method sends an email to the buyer notifying them that the payment has been received.
     *
     * @param order the order to send the email for
     */
    private void sendPaymentReceivedEmail(Order order) {
        Map<EmailPlaceholder, Object> variables = Map.of(
                EmailPlaceholder.ORDER_ID, order.getId(),
                EmailPlaceholder.RESTAURATEUR_NAME, order.getRestaurant().getRestaurateur().getName(),
                EmailPlaceholder.RESTAURATEUR_SURNAME, order.getRestaurant().getRestaurateur().getSurname(),
                EmailPlaceholder.AMOUNT, orderRepository.findAmountByOrder_Id(order.getId())
        );
        emailService.sendTemplatedEmail(
                order.getBuyer().getUser().getEmail(),
                EmailTemplateType.PAYMENT_RECEIVED,
                variables
        );
    }

    /**
     * Checks if the user has access to prepare the order.
     * <p>
     * This method throws a {@link ForbiddenOrderAccessException} if the user does not have access to prepare the order.
     *
     * @param user  the user to check
     * @param order the order to check access for
     * @throws ForbiddenOrderAccessException if access to prepare the order is forbidden
     */
    private void checkPrepareAccessOrThrow(User user, Order order) {
        if (!UserRoleUtils.isCook(user)) return;
        if (isEmployeeOfRestaurant(user, order.getRestaurant())) return;

        throw new ForbiddenOrderAccessException();
    }

    /**
     * Checks if the user has access to complete the order.
     * <p>
     * This method throws a {@link ForbiddenOrderAccessException} if the user does not have access to complete the order.
     *
     * @param user  the user to check
     * @param order the order to check access for
     * @throws ForbiddenOrderAccessException if access to complete the order is forbidden
     */
    private void checkCompleteAccessOrThrow(User user, Order order) {
        if (!UserRoleUtils.isCook(user)) return;
        if (isEmployeeOfRestaurant(user, order.getRestaurant())) return;

        throw new ForbiddenOrderAccessException();
    }

    /**
     * Checks if the user is an employee of the restaurant.
     *
     * @param user       the user to check
     * @param restaurant the restaurant to check
     * @return true if the user is an employee of the restaurant, false otherwise
     */
    private boolean isEmployeeOfRestaurant(User user, Restaurant restaurant) {
        return restaurant.getEmployees().stream().anyMatch(employeeUser -> employeeUser.getId() == user.getId());
    }

    /**
     * Notifies the order created listeners.
     * <p>
     * This method subscribes the order created listeners and notifies them.
     *
     * @param order the order to notify listeners for
     */
    private void notifyOrderCreatedListeners(Order order) {
        subscribeOrderCreatedListeners(order);
        eventManager.notifyListeners(EventType.ORDER_CREATED, order);
    }

    /**
     * Subscribes the order created listeners.
     * <p>
     * This method subscribes the order created listeners for the order.
     *
     * @param order the order to subscribe listeners for
     */
    private void subscribeOrderCreatedListeners(Order order) {
        order.getRestaurant().getEmployees().stream()
                .filter(UserRoleUtils::isCook)
                .forEach(cookUser -> eventManager.subscribe(
                        EventType.ORDER_CREATED,
                        new CookUserOrderCreatedEventListener(emailService, (CookUser) cookUser)
                ));
    }

    /**
     * Notifies the order completed listeners.
     * <p>
     * This method subscribes the order completed listeners and notifies them.
     *
     * @param order the order to notify listeners for
     */
    private void notifyOrderCompletedListeners(Order order) {
        subscribeOrderCompletedListeners(order);
        eventManager.notifyListeners(EventType.ORDER_COMPLETED, order);
    }

    /**
     * Subscribes the order completed listeners.
     * <p>
     * This method subscribes the order completed listeners for the order.
     *
     * @param order the order to subscribe listeners for
     */
    private void subscribeOrderCompletedListeners(Order order) {
        if (UserRoleUtils.isCustomer(order.getBuyer().getUser())) {
            eventManager.subscribe(
                    EventType.ORDER_COMPLETED,
                    new CustomerUserOrderCompletedEventListener(emailService)
            );
        }
    }
}