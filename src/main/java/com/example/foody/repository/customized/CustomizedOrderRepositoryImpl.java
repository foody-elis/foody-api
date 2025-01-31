package com.example.foody.repository.customized;

import com.example.foody.model.Order;
import com.example.foody.model.user.BuyerUser;
import com.example.foody.model.user.User;
import com.example.foody.utils.state.OrderStateUtils;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of the {@link CustomizedOrderRepository} interface.
 * <p>
 * Provides custom query methods for specific order-related operations.
 */
@AllArgsConstructor
public class CustomizedOrderRepositoryImpl implements CustomizedOrderRepository {

    private final EntityManager entityManager;

    /**
     * {@inheritDoc}
     * <p>
     * Also processes each order by setting the order state and the buyer user.
     *
     * @return a list of all orders
     */
    @Override
    public List<Order> findAll() {
        return entityManager
                .createQuery("SELECT o FROM Order o", Order.class)
                .getResultList()
                .stream()
                .peek(this::processOrder)
                .toList();
    }

    /**
     * {@inheritDoc}
     * <p>
     * Also processes the order by setting the order state and the buyer user.
     *
     * @param id the ID of the order
     * @return an {@link Optional} containing the order if found, or empty if not found
     */
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

    /**
     * {@inheritDoc}
     * <p>
     * Also processes each order by setting the order state and the buyer user.
     *
     * @param buyerId the ID of the buyer
     * @return a list of all orders by the buyer
     */
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

    /**
     * {@inheritDoc}
     * <p>
     * Also processes each order by setting the order state and the buyer user.
     *
     * @param restaurantId the ID of the restaurant
     * @return a list of all orders by the restaurant
     */
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

    /**
     * {@inheritDoc}
     * <p>
     * Also processes each order by setting the order state and the buyer user.
     *
     * @param restaurantId the ID of the restaurant
     * @param statuses     the list of statuses
     * @return a list of all orders by the restaurant with the specified statuses
     */
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

    /**
     * Processes the order by setting the order state and the buyer user.
     *
     * @param order the order entity
     */
    private void processOrder(Order order) {
        setOrderState(order);
        setBuyerUser(order.getBuyer());
    }

    /**
     * Sets the state of the order based on its status.
     *
     * @param order the order entity
     */
    private void setOrderState(Order order) {
        if (order.getState() == null && order.getStatus() != null) {
            order.setState(OrderStateUtils.getState(order.getStatus()));
        }
    }

    /**
     * Sets the buyer user of the order.
     *
     * @param buyer the buyer entity
     */
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