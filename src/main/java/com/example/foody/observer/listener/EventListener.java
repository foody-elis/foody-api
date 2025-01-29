package com.example.foody.observer.listener;

/**
 * Interface for event listeners that receive data of type T.
 *
 * @param <T> the type of data that will be received by listeners
 */
public interface EventListener<T> {

    /**
     * Called when an event occurs and data is sent to the listener.
     *
     * @param data the data that was sent to the listener
     */
    void update(T data);
}