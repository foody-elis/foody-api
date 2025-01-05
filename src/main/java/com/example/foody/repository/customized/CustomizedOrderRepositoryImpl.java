package com.example.foody.repository.customized;

import com.example.foody.model.Order;
import com.example.foody.model.user.BuyerUser;
import com.example.foody.model.user.User;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

public class CustomizedOrderRepositoryImpl implements CustomizedOrderRepository {
    private final EntityManager entityManager;

    public CustomizedOrderRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Order> findAllByDeletedAtIsNull() {
        return entityManager
                .createQuery("SELECT o FROM Order o WHERE o.deletedAt IS NULL", Order.class)
                .getResultList()
                .stream()
                .peek(this::processOrder)
                .toList();
    }

    @Override
    public Optional<Order> findByIdAndDeletedAtIsNull(long id) {
        return entityManager
                .createQuery("SELECT o FROM Order o WHERE o.id = :id AND o.deletedAt IS NULL", Order.class)
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
    public List<Order> findAllByDeletedAtIsNullAndBuyer_IdOrderByCreatedAtDesc(long buyerId) {
        return entityManager
                .createQuery("SELECT o FROM Order o WHERE o.deletedAt IS NULL AND o.buyer.id = :buyerId ORDER BY o.createdAt DESC", Order.class)
                .setParameter("buyerId", buyerId)
                .getResultList()
                .stream()
                .peek(this::processOrder)
                .toList();
    }

    @Override
    public List<Order> findAllByDeletedAtIsNullAndRestaurant_IdOrderByCreatedAtDesc(long restaurantId) {
        return entityManager
                .createQuery("SELECT o FROM Order o WHERE o.deletedAt IS NULL AND o.restaurant.id = :restaurantId ORDER BY o.createdAt DESC", Order.class)
                .setParameter("restaurantId", restaurantId)
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
            order.setState(order.getState());
        }
    }

    private void setBuyerUser(BuyerUser buyer) {
        if (buyer == null || buyer.getId() == null) return;

        entityManager
                .createQuery("SELECT u FROM User u WHERE u.id = :id AND u.deletedAt IS NULL", User.class)
                .setParameter("id", buyer.getId())
                .getResultList()
                .stream()
                .findFirst()
                .ifPresent(buyer::setUser);
    }
}