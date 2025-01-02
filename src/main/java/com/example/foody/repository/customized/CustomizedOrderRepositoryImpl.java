package com.example.foody.repository.customized;

import com.example.foody.model.Order;
import com.example.foody.model.user.BuyerUser;
import com.example.foody.model.user.User;
import com.example.foody.state.order.impl.CompletedState;
import com.example.foody.state.order.impl.CreatedState;
import com.example.foody.state.order.impl.PaidState;
import com.example.foody.state.order.impl.PreparingState;
import com.example.foody.utils.enums.OrderStatus;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.Optional;

public class CustomizedOrderRepositoryImpl implements CustomizedOrderRepository {
    private final EntityManager entityManager;

    public CustomizedOrderRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Optional<Order> findByIdAndDeletedAtIsNull(long id) {
        TypedQuery<Order> query = entityManager.createQuery(
                "select o from Order o where o.id = :id and o.deletedAt is null", Order.class);
        query.setParameter("id", id);

        Optional<Order> order = query.getResultList().stream().findFirst();
        order.ifPresent(o -> {
            setOrderState(o);
            setBuyerUser(o.getBuyer());
        });

        return order;
    }

    private void setOrderState(Order order) {
        if (order.getState() == null && order.getStatus() != null) {
            switch (order.getStatus()) {
                case OrderStatus.CREATED -> order.setState(new CreatedState());
                case OrderStatus.PAID -> order.setState(new PaidState());
                case OrderStatus.PREPARING -> order.setState(new PreparingState());
                case OrderStatus.COMPLETED -> order.setState(new CompletedState());
            }
        }
    }

    private void setBuyerUser(BuyerUser buyer) {
        if (buyer == null || buyer.getId() == null) return;

        TypedQuery<User> query = entityManager.createQuery(
                "select u from User u where u.id = :id and u.deletedAt is null", User.class);
        query.setParameter("id", buyer.getId());

        Optional<User> user = query.getResultList().stream().findFirst();
        user.ifPresent(buyer::setUser);
    }
}