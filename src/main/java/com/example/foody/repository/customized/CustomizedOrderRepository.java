package com.example.foody.repository.customized;

import com.example.foody.model.Order;

import java.util.Optional;

public interface CustomizedOrderRepository {
    Optional<Order> findByIdAndDeletedAtIsNull(long id);
}
