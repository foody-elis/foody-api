package com.example.foody.helper.impl;

import com.example.foody.helper.OrderHelper;
import com.example.foody.repository.OrderRepository;
import org.springframework.stereotype.Component;

@Component
public class OrderHelperImpl implements OrderHelper {
    private final OrderRepository orderRepository;

    public OrderHelperImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public double findAmountByOrderId(long orderId) {
        return orderRepository.findAmountByOrder_Id(orderId);
    }
}