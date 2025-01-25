package com.example.foody.utils.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum WebSocketTopics {
    TOPIC_ORDERS_PAYED(Constants.TOPIC + "/orders-payed/"),
    TOPIC_ORDERS_PREPARING(Constants.TOPIC + "/orders-preparing/"),
    TOPIC_ORDERS_COMPLETED(Constants.TOPIC + "/orders-completed/");

    private final String name;

    public static class Constants {
        public static final String TOPIC = "/topic";
    }
}
