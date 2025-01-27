package com.example.foody.service.impl;

import com.example.foody.TestDataUtil;
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
import com.example.foody.model.user.*;
import com.example.foody.observer.manager.EventManager;
import com.example.foody.repository.*;
import com.example.foody.service.EmailService;
import com.example.foody.state.order.impl.PaidState;
import com.example.foody.state.order.impl.PreparingState;
import com.example.foody.utils.enums.OrderStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceImplTest {

    @InjectMocks
    private OrderServiceImpl orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private DishRepository dishRepository;

    @Mock
    private OrderDishRepository orderDishRepository;

    @Mock
    private EventManager eventManager;

    @Mock
    private OrderMapper orderMapper;

    @Mock
    private EmailService emailService;

    @Mock
    private SimpMessagingTemplate simpMessagingTemplate;

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
    void saveWhenUserIsCustomerReturnsOrderResponseDTO() {
        // Arrange
        CustomerUser customer = TestDataUtil.createTestCustomerUser();
        OrderRequestDTO orderRequestDTO = TestDataUtil.createTestOrderRequestDTO();
        Order order = TestDataUtil.createTestOrder();
        Restaurant restaurant = TestDataUtil.createTestRestaurant();
        mockSecurityContext(customer);

        when(bookingRepository.existsActiveBookingForOrder(customer.getId(), orderRequestDTO.getRestaurantId()))
                .thenReturn(true);
        orderRequestDTO.getOrderDishes().forEach(orderDish -> {
            when(dishRepository.findById(orderDish.getDishId())).thenReturn(Optional.of(TestDataUtil.createTestDish()));
        });
        when(restaurantRepository.findByIdAndApproved(orderRequestDTO.getRestaurantId(), true))
                .thenReturn(Optional.of(restaurant));
        when(orderMapper.orderRequestDTOToOrder(orderRequestDTO)).thenReturn(order);
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(orderMapper.orderToOrderResponseDTO(order)).thenReturn(TestDataUtil.createTestOrderResponseDTO());
        orderRequestDTO.getOrderDishes().forEach(orderDish -> {
            when(orderDishRepository.save(any())).thenReturn(TestDataUtil.createTestOrderDish());
        });

        // Act
        OrderResponseDTO responseDTO = orderService.save(orderRequestDTO);

        // Assert
        assertNotNull(responseDTO);
        verify(orderRepository, times(1)).save(order);
        orderRequestDTO.getOrderDishes().forEach(orderDish -> {
            verify(dishRepository, times(1)).findById(orderDish.getDishId());
            verify(orderDishRepository, times(1)).save(any());
        });
    }

    @Test
    void saveWhenUserIsWaiterReturnsOrderResponseDTO() {
        // Arrange
        WaiterUser waiter = TestDataUtil.createTestWaiterUser();
        OrderRequestDTO orderRequestDTO = TestDataUtil.createTestOrderRequestDTO();
        Order order = TestDataUtil.createTestOrder();
        Restaurant restaurant = TestDataUtil.createTestRestaurant();
        mockSecurityContext(waiter);

        orderRequestDTO.getOrderDishes().forEach(orderDish -> {
            when(dishRepository.findById(orderDish.getDishId())).thenReturn(Optional.of(TestDataUtil.createTestDish()));
        });
        when(restaurantRepository.findByIdAndApproved(orderRequestDTO.getRestaurantId(), true))
                .thenReturn(Optional.of(restaurant));
        when(orderMapper.orderRequestDTOToOrder(orderRequestDTO)).thenReturn(order);
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(orderMapper.orderToOrderResponseDTO(order)).thenReturn(TestDataUtil.createTestOrderResponseDTO());
        orderRequestDTO.getOrderDishes().forEach(orderDish -> {
            when(orderDishRepository.save(any())).thenReturn(TestDataUtil.createTestOrderDish());
        });

        // Act
        OrderResponseDTO responseDTO = orderService.save(orderRequestDTO);

        // Assert
        assertNotNull(responseDTO);
        verify(orderRepository, times(1)).save(order);
        orderRequestDTO.getOrderDishes().forEach(orderDish -> {
            verify(dishRepository, times(1)).findById(orderDish.getDishId());
            verify(orderDishRepository, times(1)).save(any());
        });
    }

    @Test
    void saveWhenWaiterIsNotEmployedInRestaurantThrowsForbiddenOrderAccessException() {
        // Arrange
        WaiterUser waiter = TestDataUtil.createTestWaiterUser();
        WaiterUser otherWaiter = TestDataUtil.createTestWaiterUser();
        OrderRequestDTO orderRequestDTO = TestDataUtil.createTestOrderRequestDTO();
        Restaurant restaurant = TestDataUtil.createTestRestaurant();
        Order order = TestDataUtil.createTestOrder();
        otherWaiter.setId(2L);
        restaurant.setEmployees(List.of(otherWaiter));
        mockSecurityContext(waiter);

        when(restaurantRepository.findByIdAndApproved(orderRequestDTO.getRestaurantId(), true))
                .thenReturn(Optional.of(restaurant));
        when(orderMapper.orderRequestDTOToOrder(orderRequestDTO)).thenReturn(order);

        // Act & Assert
        assertThrows(ForbiddenOrderAccessException.class, () -> orderService.save(orderRequestDTO));
        verify(restaurantRepository, times(1)).findByIdAndApproved(orderRequestDTO.getRestaurantId(), true);
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void saveWhenRestaurantNotFoundThrowsEntityNotFoundException() {
        // Arrange
        OrderRequestDTO orderRequestDTO = TestDataUtil.createTestOrderRequestDTO();
        when(restaurantRepository.findByIdAndApproved(orderRequestDTO.getRestaurantId(), true))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> orderService.save(orderRequestDTO));
    }

    @Test
    void saveWhenActiveBookingDoesNotExistThrowsOrderNotAllowedException() {
        // Arrange
        CustomerUser customer = TestDataUtil.createTestCustomerUser();
        OrderRequestDTO orderRequestDTO = TestDataUtil.createTestOrderRequestDTO();
        Restaurant restaurant = TestDataUtil.createTestRestaurant();
        Order order = TestDataUtil.createTestOrder();
        mockSecurityContext(customer);

        when(orderMapper.orderRequestDTOToOrder(orderRequestDTO)).thenReturn(order);
        when(restaurantRepository.findByIdAndApproved(orderRequestDTO.getRestaurantId(), true))
                .thenReturn(Optional.of(restaurant));
        when(bookingRepository.existsActiveBookingForOrder(customer.getId(), orderRequestDTO.getRestaurantId()))
                .thenReturn(false);

        // Act & Assert
        assertThrows(OrderNotAllowedException.class, () -> orderService.save(orderRequestDTO));
        verify(restaurantRepository, times(1))
                .findByIdAndApproved(orderRequestDTO.getRestaurantId(), true);
        verify(bookingRepository, times(1))
                .existsActiveBookingForOrder(customer.getId(), orderRequestDTO.getRestaurantId());
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void saveWhenDishNotBelongingToRestaurantThrowsOrderNotAllowedException() {
        // Arrange
        CustomerUser customer = TestDataUtil.createTestCustomerUser();
        OrderRequestDTO orderRequestDTO = TestDataUtil.createTestOrderRequestDTO();
        Restaurant restaurant = TestDataUtil.createTestRestaurant();
        Restaurant otherRestaurant = TestDataUtil.createTestRestaurant();
        otherRestaurant.setId(2L);

        Dish dishNotBelonging = TestDataUtil.createTestDish();
        dishNotBelonging.setRestaurant(otherRestaurant);

        Order order = TestDataUtil.createTestOrder();
        order.getOrderDishes().forEach(orderDish -> orderDish.setDish(dishNotBelonging));

        mockSecurityContext(customer);

        when(restaurantRepository.findByIdAndApproved(orderRequestDTO.getRestaurantId(), true))
                .thenReturn(Optional.of(restaurant));
        when(bookingRepository.existsActiveBookingForOrder(customer.getId(), orderRequestDTO.getRestaurantId()))
                .thenReturn(true);
        when(orderMapper.orderRequestDTOToOrder(orderRequestDTO)).thenReturn(order);

        // Act & Assert
        assertThrows(OrderNotAllowedException.class, () -> orderService.save(orderRequestDTO));
        verify(restaurantRepository, times(1))
                .findByIdAndApproved(orderRequestDTO.getRestaurantId(), true);
        verify(bookingRepository, times(1))
                .existsActiveBookingForOrder(customer.getId(), orderRequestDTO.getRestaurantId());
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void saveWhenOrderDishSaveFailsThrowsEntityCreationException() {
        // Arrange
        CustomerUser customer = TestDataUtil.createTestCustomerUser();
        OrderRequestDTO orderRequestDTO = TestDataUtil.createTestOrderRequestDTO();
        Restaurant restaurant = TestDataUtil.createTestRestaurant();
        Order order = TestDataUtil.createTestOrder();
        mockSecurityContext(customer);

        when(bookingRepository.existsActiveBookingForOrder(customer.getId(), orderRequestDTO.getRestaurantId()))
                .thenReturn(true);
        when(restaurantRepository.findByIdAndApproved(orderRequestDTO.getRestaurantId(), true))
                .thenReturn(Optional.of(restaurant));
        when(orderMapper.orderRequestDTOToOrder(orderRequestDTO)).thenReturn(order);
        when(orderRepository.save(order)).thenReturn(order);
        orderRequestDTO.getOrderDishes().forEach(orderDish -> {
            when(dishRepository.findById(orderDish.getDishId())).thenReturn(Optional.of(TestDataUtil.createTestDish()));
        });
        doThrow(new RuntimeException()).when(orderDishRepository).save(any());

        // Act & Assert
        assertThrows(EntityCreationException.class, () -> orderService.save(orderRequestDTO));
        verify(orderRepository, times(1)).save(order);
        orderRequestDTO.getOrderDishes().forEach(orderDish -> {
            verify(dishRepository, times(1)).findById(orderDish.getDishId());
            verify(orderDishRepository, times(1)).save(any());
        });
    }

    @Test
    void saveWhenOrderCreationFailsThrowsEntityCreationException() {
        // Arrange
        CustomerUser customer = TestDataUtil.createTestCustomerUser();
        OrderRequestDTO orderRequestDTO = TestDataUtil.createTestOrderRequestDTO();
        Restaurant restaurant = TestDataUtil.createTestRestaurant();
        Order order = TestDataUtil.createTestOrder();
        mockSecurityContext(customer);

        when(bookingRepository.existsActiveBookingForOrder(customer.getId(), orderRequestDTO.getRestaurantId()))
                .thenReturn(true);
        when(restaurantRepository.findByIdAndApproved(orderRequestDTO.getRestaurantId(), true))
                .thenReturn(Optional.of(restaurant));
        when(orderMapper.orderRequestDTOToOrder(orderRequestDTO)).thenReturn(order);
        doThrow(new RuntimeException()).when(orderRepository).save(order);

        // Act & Assert
        assertThrows(EntityCreationException.class, () -> orderService.save(orderRequestDTO));
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    void findAllWhenOrdersExistReturnsOrderResponseDTOs() {
        // Arrange
        List<Order> orders = List.of(TestDataUtil.createTestOrder());
        when(orderRepository.findAll()).thenReturn(orders);
        when(orderMapper.ordersToOrderResponseDTOs(orders)).thenReturn(List.of(TestDataUtil.createTestOrderResponseDTO()));

        // Act
        List<OrderResponseDTO> responseDTOs = orderService.findAll();

        // Assert
        assertNotNull(responseDTOs);
        assertEquals(1, responseDTOs.size());
    }

    @Test
    void findByIdWhenUserIsCustomerReturnsOrderResponseDTO() {
        // Arrange
        CustomerUser customer = TestDataUtil.createTestCustomerUser();
        Order order = TestDataUtil.createTestOrder();
        mockSecurityContext(customer);

        when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));
        when(orderMapper.orderToOrderResponseDTO(order)).thenReturn(TestDataUtil.createTestOrderResponseDTO());

        // Act
        OrderResponseDTO responseDTO = orderService.findById(order.getId());

        // Assert
        assertNotNull(responseDTO);
        verify(orderRepository, times(1)).findById(order.getId());
    }

    @Test
    void findByIdWhenUserIsRestaurateurReturnsOrderResponseDTO() {
        // Arrange
        RestaurateurUser restaurateur = TestDataUtil.createTestRestaurateurUser();
        Order order = TestDataUtil.createTestOrder();
        Restaurant restaurant = TestDataUtil.createTestRestaurant();
        restaurateur.setId(2L);
        restaurant.setRestaurateur(restaurateur);
        order.setRestaurant(restaurant);
        mockSecurityContext(restaurateur);

        when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));
        when(orderMapper.orderToOrderResponseDTO(order)).thenReturn(TestDataUtil.createTestOrderResponseDTO());

        // Act
        OrderResponseDTO responseDTO = orderService.findById(order.getId());

        // Assert
        assertNotNull(responseDTO);
        verify(orderRepository, times(1)).findById(order.getId());
    }

    @Test
    void findByIdWhenUserIsWaiterReturnsOrderResponseDTO() {
        // Arrange
        WaiterUser waiter = TestDataUtil.createTestWaiterUser();
        Order order = TestDataUtil.createTestOrder();
        Restaurant restaurant = TestDataUtil.createTestRestaurant();
        waiter.setId(2L);
        restaurant.setEmployees(List.of(waiter));
        order.setRestaurant(restaurant);
        mockSecurityContext(waiter);

        when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));
        when(orderMapper.orderToOrderResponseDTO(order)).thenReturn(TestDataUtil.createTestOrderResponseDTO());

        // Act
        OrderResponseDTO responseDTO = orderService.findById(order.getId());

        // Assert
        assertNotNull(responseDTO);
        verify(orderRepository, times(1)).findById(order.getId());
    }

    @Test
    void findByIdWhenOrderNotFoundThrowsEntityNotFoundException() {
        // Arrange
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> orderService.findById(1L));
    }

    @Test
    void findByIdWhenUserIsNotRestaurateurNorBuyerReturnsOrderResponseDTO() {
        // Arrange
        AdminUser admin = TestDataUtil.createTestAdminUser();
        Order order = TestDataUtil.createTestOrder();
        mockSecurityContext(admin);

        when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));
        when(orderMapper.orderToOrderResponseDTO(order)).thenReturn(TestDataUtil.createTestOrderResponseDTO());

        // Act
        OrderResponseDTO responseDTO = orderService.findById(order.getId());

        // Assert
        assertNotNull(responseDTO);
        verify(orderRepository, times(1)).findById(order.getId());
    }

    @Test
    void findByIdWhenBuyerIsNotOrderOwnerThrowsForbiddenOrderAccessException() {
        // Arrange
        CustomerUser customer = TestDataUtil.createTestCustomerUser();
        CustomerUser otherCustomer = TestDataUtil.createTestCustomerUser();
        Order order = TestDataUtil.createTestOrder();
        customer.setId(2L);
        otherCustomer.setId(3L);
        order.getBuyer().setId(otherCustomer.getId());
        order.getBuyer().setUser(otherCustomer);
        mockSecurityContext(customer);

        when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));

        // Act & Assert
        assertThrows(ForbiddenOrderAccessException.class, () -> orderService.findById(order.getId()));
        verify(orderRepository, times(1)).findById(order.getId());
    }

    @Test
    void findByIdWhenRestaurateurIsNotRestaurantOwnerThrowsForbiddenOrderAccessException() {
        // Arrange
        RestaurateurUser restaurateur = TestDataUtil.createTestRestaurateurUser();
        RestaurateurUser otherRestaurateur = TestDataUtil.createTestRestaurateurUser();
        Order order = TestDataUtil.createTestOrder();
        Restaurant restaurant = TestDataUtil.createTestRestaurant();
        restaurateur.setId(2L);
        otherRestaurateur.setId(3L);
        restaurant.setRestaurateur(otherRestaurateur);
        order.setRestaurant(restaurant);
        mockSecurityContext(restaurateur);

        when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));

        // Act & Assert
        assertThrows(ForbiddenOrderAccessException.class, () -> orderService.findById(order.getId()));
        verify(orderRepository, times(1)).findById(order.getId());
    }

    @Test
    void findByIdWhenWaiterIsNotEmployedInRestaurantThrowsForbiddenOrderAccessException() {
        // Arrange
        WaiterUser waiter = TestDataUtil.createTestWaiterUser();
        WaiterUser otherWaiter = TestDataUtil.createTestWaiterUser();
        Order order = TestDataUtil.createTestOrder();
        Restaurant restaurant = TestDataUtil.createTestRestaurant();
        waiter.setId(2L);
        otherWaiter.setId(3L);
        restaurant.setEmployees(List.of(otherWaiter));
        mockSecurityContext(waiter);

        when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));

        // Act & Assert
        assertThrows(ForbiddenOrderAccessException.class, () -> orderService.findById(order.getId()));
        verify(orderRepository, times(1)).findById(order.getId());
    }

    @Test
    void findAllByBuyerWhenOrdersExistReturnsOrderResponseDTOs() {
        // Arrange
        CustomerUser customer = TestDataUtil.createTestCustomerUser();
        List<Order> orders = List.of(TestDataUtil.createTestOrder());

        when(orderRepository.findAllByBuyer_IdOrderByCreatedAtDesc(customer.getId())).thenReturn(orders);
        when(orderMapper.ordersToOrderResponseDTOs(orders)).thenReturn(List.of(TestDataUtil.createTestOrderResponseDTO()));

        // Act
        List<OrderResponseDTO> responseDTOs = orderService.findAllByBuyer(customer.getId());

        // Assert
        assertNotNull(responseDTOs);
        assertEquals(1, responseDTOs.size());
    }

    @Test
    void findAllByRestaurantWhenUserIsRestaurateurReturnsOrderResponseDTOs() {
        // Arrange
        RestaurateurUser restaurateur = TestDataUtil.createTestRestaurateurUser();
        List<Order> orders = List.of(TestDataUtil.createTestOrder());
        Restaurant restaurant = TestDataUtil.createTestRestaurant();
        mockSecurityContext(restaurateur);

        when(restaurantRepository.findByIdAndApproved(restaurant.getId(), true)).thenReturn(Optional.of(restaurant));
        when(orderRepository.findAllByRestaurant_IdOrderByCreatedAtDesc(restaurant.getId())).thenReturn(orders);
        when(orderMapper.ordersToOrderResponseDTOs(orders)).thenReturn(List.of(TestDataUtil.createTestOrderResponseDTO()));

        // Act
        List<OrderResponseDTO> responseDTOs = orderService.findAllByRestaurant(restaurant.getId());

        // Assert
        assertNotNull(responseDTOs);
        assertEquals(1, responseDTOs.size());
    }

    @Test
    void findAllByRestaurantWhenUserIsCookReturnsOrderResponseDTOs() {
        // Arrange
        CookUser cook = TestDataUtil.createTestCookUser();
        List<Order> orders = List.of(TestDataUtil.createTestOrder());
        Restaurant restaurant = TestDataUtil.createTestRestaurant();
        cook.setId(2L);
        restaurant.setEmployees(List.of(cook));
        mockSecurityContext(cook);

        when(restaurantRepository.findByIdAndApproved(restaurant.getId(), true)).thenReturn(Optional.of(restaurant));
        when(orderRepository.findAllByRestaurant_IdOrderByCreatedAtDesc(restaurant.getId())).thenReturn(orders);
        when(orderMapper.ordersToOrderResponseDTOs(orders)).thenReturn(List.of(TestDataUtil.createTestOrderResponseDTO()));

        // Act
        List<OrderResponseDTO> responseDTOs = orderService.findAllByRestaurant(restaurant.getId());

        // Assert
        assertNotNull(responseDTOs);
        assertEquals(1, responseDTOs.size());
    }

    @Test
    void findAllByRestaurantWhenUserIsNotRestaurateurNorEmployeeReturnsOrderResponseDTOs() {
        // Arrange
        AdminUser admin = TestDataUtil.createTestAdminUser();
        List<Order> orders = List.of(TestDataUtil.createTestOrder());
        Restaurant restaurant = TestDataUtil.createTestRestaurant();
        mockSecurityContext(admin);

        when(restaurantRepository.findByIdAndApproved(restaurant.getId(), true)).thenReturn(Optional.of(restaurant));
        when(orderRepository.findAllByRestaurant_IdOrderByCreatedAtDesc(restaurant.getId())).thenReturn(orders);
        when(orderMapper.ordersToOrderResponseDTOs(orders)).thenReturn(List.of(TestDataUtil.createTestOrderResponseDTO()));

        // Act
        List<OrderResponseDTO> responseDTOs = orderService.findAllByRestaurant(restaurant.getId());

        // Assert
        assertNotNull(responseDTOs);
        assertEquals(1, responseDTOs.size());
    }

    @Test
    void findAllByRestaurantWhenRestaurateurIsNotRestaurantOwnerThrowsForbiddenRestaurantAccessException() {
        // Arrange
        RestaurateurUser restaurateur = TestDataUtil.createTestRestaurateurUser();
        RestaurateurUser otherRestaurateur = TestDataUtil.createTestRestaurateurUser();
        Restaurant restaurant = TestDataUtil.createTestRestaurant();
        restaurant.setEmployees(List.of());
        otherRestaurateur.setId(2L);
        restaurant.setRestaurateur(otherRestaurateur);
        mockSecurityContext(restaurateur);

        when(restaurantRepository.findByIdAndApproved(restaurant.getId(), true)).thenReturn(Optional.of(restaurant));

        // Act & Assert
        assertThrows(ForbiddenRestaurantAccessException.class, () -> orderService.findAllByRestaurant(restaurant.getId()));
        verify(restaurantRepository, times(1)).findByIdAndApproved(restaurant.getId(), true);
        verify(orderRepository, never()).findAllByRestaurant_IdOrderByCreatedAtDesc(restaurant.getId());
    }

    @Test
    void findAllByRestaurantWhenCookIsNotEmployedInRestaurantThrowsForbiddenRestaurantAccessException() {
        // Arrange
        CookUser cook = TestDataUtil.createTestCookUser();
        CookUser otherCook = TestDataUtil.createTestCookUser();
        Restaurant restaurant = TestDataUtil.createTestRestaurant();
        cook.setId(2L);
        otherCook.setId(3L);
        restaurant.setEmployees(List.of(otherCook));
        mockSecurityContext(cook);

        when(restaurantRepository.findByIdAndApproved(restaurant.getId(), true)).thenReturn(Optional.of(restaurant));

        // Act & Assert
        assertThrows(ForbiddenRestaurantAccessException.class, () -> orderService.findAllByRestaurant(restaurant.getId()));
        verify(restaurantRepository, times(1)).findByIdAndApproved(restaurant.getId(), true);
        verify(orderRepository, never()).findAllByRestaurant_IdOrderByCreatedAtDesc(restaurant.getId());
    }

    @Test
    void findAllByRestaurantAndInProgressWhenUserIsCookReturnsOrderResponseDTOs() {
        // Arrange
        CookUser cook = TestDataUtil.createTestCookUser();
        List<Order> orders = List.of(TestDataUtil.createTestOrder());
        Restaurant restaurant = TestDataUtil.createTestRestaurant();
        cook.setId(2L);
        restaurant.setEmployees(List.of(cook));
        mockSecurityContext(cook);

        when(restaurantRepository.findByIdAndApproved(restaurant.getId(), true)).thenReturn(Optional.of(restaurant));
        when(orderRepository.findAllByRestaurant_IdAndStatusInOrderByCreatedAtDesc(
                restaurant.getId(),
                List.of(OrderStatus.PAID.name(), OrderStatus.PREPARING.name()))
        ).thenReturn(orders);
        when(orderMapper.ordersToOrderResponseDTOs(orders)).thenReturn(List.of(TestDataUtil.createTestOrderResponseDTO()));

        // Act
        List<OrderResponseDTO> responseDTOs = orderService.findAllByRestaurantAndInProgress(restaurant.getId());

        // Assert
        assertNotNull(responseDTOs);
        assertEquals(1, responseDTOs.size());
    }

    @Test
    void payByIdWhenUserIsBuyerReturnsOrderResponseDTO() {
        // Arrange
        CustomerUser customer = TestDataUtil.createTestCustomerUser();
        Order order = TestDataUtil.createTestOrder();
        order.getBuyer().setUser(customer);
        mockSecurityContext(customer);

        when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));
        when(orderRepository.save(order)).thenReturn(order);
        when(orderMapper.orderToOrderResponseDTO(order)).thenReturn(TestDataUtil.createTestOrderResponseDTO());

        // Act
        OrderResponseDTO responseDTO = orderService.payById(order.getId());

        // Assert
        assertNotNull(responseDTO);
        verify(orderRepository, times(1)).findById(order.getId());
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    void payByIdWhenOrderNotFoundThrowsEntityNotFoundException() {
        // Arrange
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> orderService.payById(1L));
    }

    @Test
    void payByIdWhenUserIsNotBuyerReturnsOrderResponseDTO() {
        // Arrange
        AdminUser admin = TestDataUtil.createTestAdminUser();
        CustomerUser customer = TestDataUtil.createTestCustomerUser();
        Order order = TestDataUtil.createTestOrder();
        order.getBuyer().setUser(customer);
        mockSecurityContext(admin);

        when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));
        when(orderRepository.save(order)).thenReturn(order);
        when(orderMapper.orderToOrderResponseDTO(order)).thenReturn(TestDataUtil.createTestOrderResponseDTO());

        // Act
        OrderResponseDTO responseDTO = orderService.payById(order.getId());

        // Assert
        assertNotNull(responseDTO);
        verify(orderRepository, times(1)).findById(order.getId());
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    void payByIdWhenBuyerIsNotOrderOwnerThrowsForbiddenOrderAccessException() {
        // Arrange
        CustomerUser customer = TestDataUtil.createTestCustomerUser();
        CustomerUser otherCustomer = TestDataUtil.createTestCustomerUser();
        Order order = TestDataUtil.createTestOrder();
        otherCustomer.setId(2L);
        order.getBuyer().setId(otherCustomer.getId());
        order.getBuyer().setUser(otherCustomer);
        mockSecurityContext(customer);

        when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));

        // Act & Assert
        assertThrows(ForbiddenOrderAccessException.class, () -> orderService.payById(order.getId()));
        verify(orderRepository, times(1)).findById(order.getId());
        verify(orderRepository, never()).save(order);
    }

    @Test
    void payByIdWhenOrderIsAlreadyPaidThrowsInvalidOrderStateException() {
        // Arrange
        CustomerUser customer = TestDataUtil.createTestCustomerUser();
        Order order = TestDataUtil.createTestOrder();
        order.setState(new PaidState());
        order.getBuyer().setUser(customer);
        mockSecurityContext(customer);

        when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));

        // Act & Assert
        assertThrows(InvalidOrderStateException.class, () -> orderService.payById(order.getId()));
        verify(orderRepository, times(1)).findById(order.getId());
        verify(orderRepository, never()).save(order);
    }

    @Test
    void payByIdWhenSaveFailsThrowsEntityEditException() {
        // Arrange
        CustomerUser customer = TestDataUtil.createTestCustomerUser();
        Order order = TestDataUtil.createTestOrder();
        order.getBuyer().setUser(customer);
        mockSecurityContext(customer);

        when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));
        doThrow(new RuntimeException()).when(orderRepository).save(order);

        // Act & Assert
        assertThrows(EntityEditException.class, () -> orderService.payById(order.getId()));
        verify(orderRepository, times(1)).findById(order.getId());
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    void prepareByIdWhenUserIsCookReturnsOrderResponseDTO() {
        // Arrange
        CookUser cook = TestDataUtil.createTestCookUser();
        Order order = TestDataUtil.createTestOrder();
        Restaurant restaurant = TestDataUtil.createTestRestaurant();
        order.setState(new PaidState());
        cook.setId(2L);
        restaurant.setEmployees(List.of(cook));
        order.setRestaurant(restaurant);
        mockSecurityContext(cook);

        when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));
        when(orderRepository.save(order)).thenReturn(order);
        when(orderMapper.orderToOrderResponseDTO(order)).thenReturn(TestDataUtil.createTestOrderResponseDTO());

        // Act
        OrderResponseDTO responseDTO = orderService.prepareById(order.getId());

        // Assert
        assertNotNull(responseDTO);
        verify(orderRepository, times(1)).findById(order.getId());
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    void prepareByIdWhenUserIsAdminReturnsOrderResponseDTO() {
        // Arrange
        AdminUser admin = TestDataUtil.createTestAdminUser();
        Order order = TestDataUtil.createTestOrder();
        order.setState(new PaidState());
        mockSecurityContext(admin);

        when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));
        when(orderRepository.save(order)).thenReturn(order);
        when(orderMapper.orderToOrderResponseDTO(order)).thenReturn(TestDataUtil.createTestOrderResponseDTO());

        // Act
        OrderResponseDTO responseDTO = orderService.prepareById(order.getId());

        // Assert
        assertNotNull(responseDTO);
        verify(orderRepository, times(1)).findById(order.getId());
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    void prepareByIdWhenCookIsNotEmployedInRestaurantThrowsForbiddenOrderAccessException() {
        // Arrange
        CookUser cook = TestDataUtil.createTestCookUser();
        CookUser otherCook = TestDataUtil.createTestCookUser();
        Order order = TestDataUtil.createTestOrder();
        Restaurant restaurant = TestDataUtil.createTestRestaurant();
        cook.setId(2L);
        otherCook.setId(3L);
        restaurant.setEmployees(List.of(otherCook));
        order.setRestaurant(restaurant);
        mockSecurityContext(cook);

        when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));

        // Act & Assert
        assertThrows(ForbiddenOrderAccessException.class, () -> orderService.prepareById(order.getId()));
        verify(orderRepository, times(1)).findById(order.getId());
        verify(orderRepository, never()).save(order);
    }

    @Test
    void prepareByIdWhenOrderIsNotPaidThrowsInvalidOrderStateException() {
        // Arrange
        CookUser cook = TestDataUtil.createTestCookUser();
        Order order = TestDataUtil.createTestOrder();
        mockSecurityContext(cook);

        when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));

        // Act & Assert
        assertThrows(InvalidOrderStateException.class, () -> orderService.prepareById(order.getId()));
        verify(orderRepository, times(1)).findById(order.getId());
        verify(orderRepository, never()).save(order);
    }

    @Test
    void prepareByIdWhenOrderIsAlreadyPreparedThrowsInvalidOrderStateException() {
        // Arrange
        CookUser cook = TestDataUtil.createTestCookUser();
        Order order = TestDataUtil.createTestOrder();
        order.setState(new PreparingState());
        mockSecurityContext(cook);

        when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));

        // Act & Assert
        assertThrows(InvalidOrderStateException.class, () -> orderService.prepareById(order.getId()));
        verify(orderRepository, times(1)).findById(order.getId());
        verify(orderRepository, never()).save(order);
    }

    @Test
    void prepareByIdWhenSaveFailsThrowsEntityEditException() {
        // Arrange
        CookUser cook = TestDataUtil.createTestCookUser();
        Order order = TestDataUtil.createTestOrder();
        order.setState(new PaidState());
        mockSecurityContext(cook);

        when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));
        doThrow(new RuntimeException()).when(orderRepository).save(order);

        // Act & Assert
        assertThrows(EntityEditException.class, () -> orderService.prepareById(order.getId()));
        verify(orderRepository, times(1)).findById(order.getId());
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    void completeByIdWhenUserIsCookReturnsOrderResponseDTO() {
        // Arrange
        CookUser cook = TestDataUtil.createTestCookUser();
        Order order = TestDataUtil.createTestOrder();
        Restaurant restaurant = TestDataUtil.createTestRestaurant();
        order.getBuyer().setUser(TestDataUtil.createTestCustomerUser());
        order.setState(new PreparingState());
        cook.setId(2L);
        restaurant.setEmployees(List.of(cook));
        order.setRestaurant(restaurant);
        mockSecurityContext(cook);

        when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));
        when(orderRepository.save(order)).thenReturn(order);
        when(orderMapper.orderToOrderResponseDTO(order)).thenReturn(TestDataUtil.createTestOrderResponseDTO());

        // Act
        OrderResponseDTO responseDTO = orderService.completeById(order.getId());

        // Assert
        assertNotNull(responseDTO);
        verify(orderRepository, times(1)).findById(order.getId());
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    void completeByIdWhenUserIsAdminReturnsOrderResponseDTO() {
        // Arrange
        AdminUser admin = TestDataUtil.createTestAdminUser();
        Order order = TestDataUtil.createTestOrder();
        order.getBuyer().setUser(TestDataUtil.createTestCustomerUser());
        order.setState(new PreparingState());
        mockSecurityContext(admin);

        when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));
        when(orderRepository.save(order)).thenReturn(order);
        when(orderMapper.orderToOrderResponseDTO(order)).thenReturn(TestDataUtil.createTestOrderResponseDTO());

        // Act
        OrderResponseDTO responseDTO = orderService.completeById(order.getId());

        // Assert
        assertNotNull(responseDTO);
        verify(orderRepository, times(1)).findById(order.getId());
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    void completeByIdWhenBuyerIsWaiterReturnsOrderResponseDTO() {
        // Arrange
        WaiterUser waiter = TestDataUtil.createTestWaiterUser();
        Order order = TestDataUtil.createTestOrder();
        order.getBuyer().setUser(waiter);
        order.setState(new PreparingState());
        mockSecurityContext(waiter);

        when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));
        when(orderRepository.save(order)).thenReturn(order);
        when(orderMapper.orderToOrderResponseDTO(order)).thenReturn(TestDataUtil.createTestOrderResponseDTO());

        // Act
        OrderResponseDTO responseDTO = orderService.completeById(order.getId());

        // Assert
        assertNotNull(responseDTO);
        verify(orderRepository, times(1)).findById(order.getId());
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    void completeByIdWhenCookIsNotEmployedInRestaurantThrowsForbiddenOrderAccessException() {
        // Arrange
        CookUser cook = TestDataUtil.createTestCookUser();
        CookUser otherCook = TestDataUtil.createTestCookUser();
        Order order = TestDataUtil.createTestOrder();
        Restaurant restaurant = TestDataUtil.createTestRestaurant();
        cook.setId(2L);
        otherCook.setId(3L);
        restaurant.setEmployees(List.of(otherCook));
        order.setRestaurant(restaurant);
        mockSecurityContext(cook);

        when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));

        // Act & Assert
        assertThrows(ForbiddenOrderAccessException.class, () -> orderService.completeById(order.getId()));
        verify(orderRepository, times(1)).findById(order.getId());
        verify(orderRepository, never()).save(order);
    }

    @Test
    void completeByIdWhenOrderIsNotPreparingThrowsInvalidOrderStateException() {
        // Arrange
        CookUser cook = TestDataUtil.createTestCookUser();
        Order order = TestDataUtil.createTestOrder();
        order.getBuyer().setUser(TestDataUtil.createTestCustomerUser());
        mockSecurityContext(cook);

        when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));

        // Act & Assert
        assertThrows(InvalidOrderStateException.class, () -> orderService.completeById(order.getId()));
        verify(orderRepository, times(1)).findById(order.getId());
        verify(orderRepository, never()).save(order);
    }

    @Test
    void completeByIdWhenOrderIsAlreadyCompletedThrowsInvalidOrderStateException() {
        // Arrange
        CookUser cook = TestDataUtil.createTestCookUser();
        Order order = TestDataUtil.createTestOrder();
        order.getBuyer().setUser(TestDataUtil.createTestCustomerUser());
        order.setState(new PaidState());
        mockSecurityContext(cook);

        when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));

        // Act & Assert
        assertThrows(InvalidOrderStateException.class, () -> orderService.completeById(order.getId()));
        verify(orderRepository, times(1)).findById(order.getId());
        verify(orderRepository, never()).save(order);
    }

    @Test
    void completeByIdWhenSaveFailsThrowsEntityEditException() {
        // Arrange
        CookUser cook = TestDataUtil.createTestCookUser();
        Order order = TestDataUtil.createTestOrder();
        order.getBuyer().setUser(TestDataUtil.createTestCustomerUser());
        order.setState(new PreparingState());
        mockSecurityContext(cook);

        when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));
        doThrow(new RuntimeException()).when(orderRepository).save(order);

        // Act & Assert
        assertThrows(EntityEditException.class, () -> orderService.completeById(order.getId()));
        verify(orderRepository, times(1)).findById(order.getId());
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    void removeWhenOrderExistsReturnsTrue() {
        // Arrange
        Order order = TestDataUtil.createTestOrder();

        when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));

        // Act
        boolean result = orderService.remove(order.getId());

        // Assert
        assertTrue(result);
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    void removeWhenSaveFailsThrowsEntityDeletionException() {
        // Arrange
        Order order = TestDataUtil.createTestOrder();

        when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));
        doThrow(new RuntimeException()).when(orderRepository).save(order);

        // Act & Assert
        assertThrows(EntityDeletionException.class, () -> orderService.remove(order.getId()));
        verify(orderRepository, times(1).description("Save should be called once")).save(order);
    }
}