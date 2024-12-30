package com.example.foody.observer.listener;

// T - type of data that will be received by listeners
public interface EventListener<T> {
    void update(T data);
}