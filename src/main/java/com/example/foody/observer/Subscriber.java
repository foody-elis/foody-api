package com.example.foody.observer;

// T - type of data that will be received by subscribers
public interface Subscriber<T> {
    void update(T data);
}