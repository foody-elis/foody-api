package com.example.foody.observer;

// T - type of data that will be sent to subscribers
public interface Publisher<T> {
    void subscribe(Subscriber<T> subscriber);
    void unsubscribe(Subscriber<T> subscriber);
    void notifySubscribers();
}