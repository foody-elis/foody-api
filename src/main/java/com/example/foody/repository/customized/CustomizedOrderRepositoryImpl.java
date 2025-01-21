package com.example.foody.repository.customized;

import com.example.foody.model.Order;
import com.example.foody.model.user.BuyerUser;
import com.example.foody.model.user.User;
import com.example.foody.utils.enums.OrderStatus;
import com.example.foody.utils.state.OrderStateUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class CustomizedOrderRepositoryImpl implements CustomizedOrderRepository {
    private final EntityManager entityManager;

    public CustomizedOrderRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Order> findAll() {
        return entityManager
                .createQuery("SELECT o FROM Order o", Order.class)
                .getResultList()
                .stream()
                .peek(this::processOrder)
                .toList();
    }

    @Override
    public Optional<Order> findById(long id) {
        return entityManager
                .createQuery("SELECT o FROM Order o WHERE o.id = :id", Order.class)
                .setParameter("id", id)
                .getResultList()
                .stream()
                .findFirst()
                .map(o -> {
                    processOrder(o);
                    return o;
                });
    }

    @Override
    public List<Order> findAllByBuyer_IdOrderByCreatedAtDesc(long buyerId) {
        return entityManager
                .createQuery("SELECT o FROM Order o WHERE o.buyer.id = :buyerId ORDER BY o.createdAt DESC", Order.class)
                .setParameter("buyerId", buyerId)
                .getResultList()
                .stream()
                .peek(this::processOrder)
                .toList();
    }

    @Override
    public List<Order> findAllByRestaurant_IdOrderByCreatedAtDesc(long restaurantId) {
        return entityManager
                .createQuery("SELECT o FROM Order o WHERE o.restaurant.id = :restaurantId ORDER BY o.createdAt DESC", Order.class)
                .setParameter("restaurantId", restaurantId)
                .getResultList()
                .stream()
                .peek(this::processOrder)
                .toList();
    }

    @Override
    public List<Order> findAllByRestaurant_IdAndStatusInOrderByCreatedAtDesc(long restaurantId, List<String> statuses) {
        return entityManager
                .createQuery("""
                        SELECT o
                        FROM Order o
                        WHERE o.restaurant.id = :restaurantId
                        AND o.status IN :statuses
                        ORDER BY o.createdAt DESC
                        """, Order.class)
                .setParameter("restaurantId", restaurantId)
                .setParameter("statuses", statuses)
                .getResultList()
                .stream()
                .peek(this::processOrder)
                .toList();
    }

    private void processOrder(Order order) {
        setOrderState(order);
        setBuyerUser(order.getBuyer());
    }

    private void setOrderState(Order order) {
        if (order.getState() == null && order.getStatus() != null) {
            order.setState(OrderStateUtils.getState(order.getStatus()));
        }
    }

    private void setBuyerUser(BuyerUser buyer) {
        if (buyer == null || buyer.getId() == null) return;

        entityManager
                .createQuery("SELECT u FROM User u WHERE u.id = :id", User.class)
                .setParameter("id", buyer.getId())
                .getResultList()
                .stream()
                .findFirst()
                .ifPresent(buyer::setUser);
    }
}