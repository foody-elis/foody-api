package com.example.foody.repository.customized;

import com.example.foody.model.Order;
import com.example.foody.state.order.impl.AwaitingPaymentState;
import com.example.foody.state.order.impl.CompletedState;
import com.example.foody.state.order.OrderStatus;
import com.example.foody.state.order.impl.PreparingState;
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
            if (o.getState() == null && o.getStatus() != null) {
                switch (o.getStatus()) {
                    case OrderStatus.PREPARING -> o.setState(new PreparingState(o));
                    case OrderStatus.AWAITING_PAYMENT -> o.setState(new AwaitingPaymentState(o));
                    case OrderStatus.COMPLETED -> o.setState(new CompletedState(o));
                }
            }
        });

        return order;
    }
}
