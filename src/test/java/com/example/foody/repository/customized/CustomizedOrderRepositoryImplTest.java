package com.example.foody.repository.customized;

import com.example.foody.model.Order;
import com.example.foody.model.user.BuyerUser;
import com.example.foody.model.user.User;
import com.example.foody.state.order.OrderState;
import com.example.foody.utils.enums.OrderStatus;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CustomizedOrderRepositoryImplTest {

    private EntityManager entityManager;
    private CustomizedOrderRepositoryImpl repository;

    @BeforeEach
    void setUp() {
        entityManager = mock(EntityManager.class);
        repository = new CustomizedOrderRepositoryImpl(entityManager);

        // Mock the TypedQuery
        TypedQuery<Order> query = mock(TypedQuery.class);
        when(entityManager.createQuery(anyString(), eq(Order.class))).thenReturn(query);
        when(query.setParameter(anyString(), any())).thenReturn(query);
    }

    @Test
    void findAllWhenOrdersExistReturnsAllOrders() {
        // Arrange
        Order order1 = mock(Order.class);
        Order order2 = mock(Order.class);
        TypedQuery<Order> query = mock(TypedQuery.class);

        when(entityManager.createQuery("SELECT o FROM Order o", Order.class)).thenReturn(query);
        when(query.getResultList()).thenReturn(List.of(order1, order2));

        // Act
        List<Order> result = repository.findAll();

        // Assert
        assertEquals(2, result.size());
        verify(order1, times(1)).getState();
        verify(order2, times(1)).getState();
    }

    @Test
    void findByIdWhenOrderExistsReturnsOrder() {
        // Arrange
        long orderId = 1L;
        Order order = mock(Order.class);
        TypedQuery<Order> query = mock(TypedQuery.class);

        when(entityManager.createQuery("SELECT o FROM Order o WHERE o.id = :id", Order.class)).thenReturn(query);
        when(query.setParameter("id", orderId)).thenReturn(query);
        when(query.getResultList()).thenReturn(List.of(order));

        // Act
        Optional<Order> result = repository.findById(orderId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(order, result.get());
        verify(order, times(1)).getState();
    }

    @Test
    void findByIdWhenOrderDoesNotExistReturnsEmpty() {
        // Arrange
        long orderId = 1L;
        TypedQuery<Order> query = mock(TypedQuery.class);

        when(entityManager.createQuery("SELECT o FROM Order o WHERE o.id = :id", Order.class)).thenReturn(query);
        when(query.setParameter("id", orderId)).thenReturn(query);
        when(query.getResultList()).thenReturn(List.of());

        // Act
        Optional<Order> result = repository.findById(orderId);

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    void findAllByBuyer_IdOrderByCreatedAtDescWhenOrdersExistReturnsOrders() {
        // Arrange
        long buyerId = 1L;
        Order order = mock(Order.class);
        TypedQuery<Order> query = mock(TypedQuery.class);

        when(entityManager.createQuery("SELECT o FROM Order o WHERE o.buyer.id = :buyerId ORDER BY o.createdAt DESC", Order.class)).thenReturn(query);
        when(query.setParameter("buyerId", buyerId)).thenReturn(query);
        when(query.getResultList()).thenReturn(List.of(order));

        // Act
        List<Order> result = repository.findAllByBuyer_IdOrderByCreatedAtDesc(buyerId);

        // Assert
        assertEquals(1, result.size());
        verify(order, times(1)).getState();
    }

    @Test
    void setBuyerUserWhenBuyerExistsSetsUser() {
        // Arrange
        long orderId = 1L;
        Order order = mock(Order.class);
        BuyerUser buyer = mock(BuyerUser.class);
        User user = mock(User.class);
        TypedQuery<Order> orderQuery = mock(TypedQuery.class);
        TypedQuery<User> userQuery = mock(TypedQuery.class);

        // Configure the EntityManager mock to return a TypedQuery mock for Order
        when(entityManager.createQuery("SELECT o FROM Order o WHERE o.id = :id", Order.class)).thenReturn(orderQuery);
        when(orderQuery.setParameter("id", orderId)).thenReturn(orderQuery);
        when(orderQuery.getResultList()).thenReturn(List.of(order));

        // Configure the Order mock
        when(order.getBuyer()).thenReturn(buyer);
        when(buyer.getId()).thenReturn(1L);

        // Configure the EntityManager mock to return a TypedQuery mock for User
        when(entityManager.createQuery("SELECT u FROM User u WHERE u.id = :id", User.class)).thenReturn(userQuery);
        when(userQuery.setParameter("id", 1L)).thenReturn(userQuery);
        when(userQuery.getResultList()).thenReturn(List.of(user));

        // Act
        Optional<Order> result = repository.findById(orderId);

        // Assert
        assertTrue(result.isPresent());
        verify(buyer, times(1)).setUser(user);
    }

    @Test
    void setBuyerUserWhenBuyerIdIsNullDoesNotSetUser() {
        // Arrange
        long orderId = 1L;
        Order order = mock(Order.class);
        BuyerUser buyer = mock(BuyerUser.class);
        TypedQuery<Order> query = mock(TypedQuery.class);

        when(entityManager.createQuery("SELECT o FROM Order o WHERE o.id = :id", Order.class)).thenReturn(query);
        when(query.setParameter("id", orderId)).thenReturn(query);
        when(query.getResultList()).thenReturn(List.of(order));
        when(order.getBuyer()).thenReturn(buyer);
        when(buyer.getId()).thenReturn(null);

        // Act
        Optional<Order> result = repository.findById(orderId);

        // Assert
        assertTrue(result.isPresent());
        verify(buyer, never()).setUser(any());
    }

    @Test
    void setOrderStateWhenStateIsNullSetsState() {
        // Arrange
        long orderId = 1L;
        Order order = mock(Order.class);
        TypedQuery<Order> query = mock(TypedQuery.class);

        // Configure the EntityManager mock to return a TypedQuery mock
        when(entityManager.createQuery("SELECT o FROM Order o WHERE o.id = :id", Order.class)).thenReturn(query);
        when(query.setParameter("id", orderId)).thenReturn(query);
        when(query.getResultList()).thenReturn(List.of(order));

        // Configure the Order mock
        when(order.getState()).thenReturn(null);
        when(order.getStatus()).thenReturn(OrderStatus.CREATED);

        ArgumentCaptor<Object> stateCaptor = ArgumentCaptor.forClass(Object.class);

        // Act
        Optional<Order> result = repository.findById(orderId);

        // Assert
        assertTrue(result.isPresent());
        verify(order, times(1)).getState();
        verify(order, times(1)).setState((OrderState) stateCaptor.capture());
        assertTrue(stateCaptor.getValue() instanceof com.example.foody.state.order.impl.CreatedState);
    }

    @Test
    void setOrderStateWhenStateIsNotNullDoesNotOverrideState() {
        // Arrange
        long orderId = 1L;
        Order order = mock(Order.class);
        TypedQuery<Order> query = mock(TypedQuery.class);

        when(entityManager.createQuery("SELECT o FROM Order o WHERE o.id = :id", Order.class)).thenReturn(query);
        when(query.setParameter("id", orderId)).thenReturn(query);
        when(query.getResultList()).thenReturn(List.of(order));
        when(order.getState()).thenReturn(mock(com.example.foody.state.order.OrderState.class));

        // Act
        Optional<Order> result = repository.findById(orderId);

        // Assert
        assertTrue(result.isPresent());
        verify(order, never()).setState(any());
    }

    @Test
    void findAllByRestaurant_IdOrderByCreatedAtDescWhenOrdersExistReturnsOrders() {
        // Arrange
        long restaurantId = 1L;
        Order order1 = mock(Order.class);
        Order order2 = mock(Order.class);
        TypedQuery<Order> query = mock(TypedQuery.class);

        // Configure the EntityManager mock to return a TypedQuery mock
        when(entityManager.createQuery("SELECT o FROM Order o WHERE o.restaurant.id = :restaurantId ORDER BY o.createdAt DESC", Order.class))
                .thenReturn(query);
        when(query.setParameter("restaurantId", restaurantId)).thenReturn(query);
        when(query.getResultList()).thenReturn(List.of(order1, order2));

        // Act
        List<Order> result = repository.findAllByRestaurant_IdOrderByCreatedAtDesc(restaurantId);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(order1));
        assertTrue(result.contains(order2));
        verify(query, times(1)).setParameter("restaurantId", restaurantId);
        verify(query, times(1)).getResultList();
    }

    @Test
    void findAllWhenNoOrdersExistReturnsEmptyList() {
        // Arrange
        TypedQuery<Order> query = mock(TypedQuery.class);
        when(entityManager.createQuery("SELECT o FROM Order o", Order.class)).thenReturn(query);
        when(query.getResultList()).thenReturn(Collections.emptyList());

        // Act
        List<Order> result = repository.findAll();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void findAllByRestaurant_IdOrderByCreatedAtDescWhenNoOrdersExistReturnsEmptyList() {
        // Arrange
        long restaurantId = 1L;
        TypedQuery<Order> query = mock(TypedQuery.class);
        when(entityManager.createQuery("SELECT o FROM Order o WHERE o.restaurant.id = :restaurantId ORDER BY o.createdAt DESC", Order.class))
                .thenReturn(query);
        when(query.setParameter("restaurantId", restaurantId)).thenReturn(query);
        when(query.getResultList()).thenReturn(Collections.emptyList());

        // Act
        List<Order> result = repository.findAllByRestaurant_IdOrderByCreatedAtDesc(restaurantId);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void findAllByRestaurant_IdAndStatusInOrderByCreatedAtDescWhenStatusesEmptyReturnsEmptyList() {
        // Arrange
        long restaurantId = 1L;
        List<String> statuses = Collections.emptyList();
        TypedQuery<Order> query = mock(TypedQuery.class);
        when(entityManager.createQuery("SELECT o FROM Order o WHERE o.restaurant.id = :restaurantId AND o.status IN :statuses ORDER BY o.createdAt DESC", Order.class))
                .thenReturn(query);
        when(query.setParameter("restaurantId", restaurantId)).thenReturn(query);
        when(query.setParameter("statuses", statuses)).thenReturn(query);
        when(query.getResultList()).thenReturn(Collections.emptyList());

        // Act
        List<Order> result = repository.findAllByRestaurant_IdAndStatusInOrderByCreatedAtDesc(restaurantId, statuses);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void setBuyerUserWhenBuyerIsNullDoesNotThrowException() {
        // Arrange
        long orderId = 1L;
        Order order = mock(Order.class);
        TypedQuery<Order> query = mock(TypedQuery.class);

        when(entityManager.createQuery("SELECT o FROM Order o WHERE o.id = :id", Order.class)).thenReturn(query);
        when(query.setParameter("id", orderId)).thenReturn(query);
        when(query.getResultList()).thenReturn(List.of(order));
        when(order.getBuyer()).thenReturn(null);

        // Act
        Optional<Order> result = repository.findById(orderId);

        // Assert
        assertTrue(result.isPresent());
    }
}