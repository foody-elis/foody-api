package com.example.foody.observer.manager;

import com.example.foody.observer.listener.EventListener;
import com.example.foody.utils.enums.EventType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Manages event listeners and notifies them of events.
 */
@Getter
@Setter
@Component
public class EventManager {

    /**
     * Map of event types to lists of event listeners.
     */
    private final Map<EventType, List<EventListener<?>>> listeners = new EnumMap<>(EventType.class);

    /**
     * Initializes the EventManager with empty listener lists for each event type.
     */
    public EventManager() {
        Arrays.stream(EventType.values()).forEach(eventType ->
                listeners.put(eventType, new ArrayList<>())
        );
    }

    /**
     * Subscribes a listener to a specific event type.
     *
     * @param eventType the type of event to subscribe to
     * @param listener the listener to notify of the event
     * @param <T> the type of data that will be sent to listeners
     */
    public <T> void subscribe(EventType eventType, EventListener<T> listener) {
        List<EventListener<?>> eventListeners = listeners.get(eventType);
        eventListeners.add(listener);
    }

    /**
     * Unsubscribes a listener from a specific event type.
     *
     * @param eventType the type of event to unsubscribe from
     * @param eventListener the listener to remove
     * @param <T> the type of data that was sent to listeners
     */
    public <T> void unsubscribe(EventType eventType, EventListener<T> eventListener) {
        List<EventListener<?>> eventListeners = listeners.get(eventType);
        eventListeners.remove(eventListener);
    }

    /**
     * Notifies all listeners of a specific event type with the provided data.
     *
     * @param eventType the type of event to notify listeners of
     * @param data the data to send to listeners
     * @param <T> the type of data that will be sent to listeners
     */
    public <T> void notifyListeners(EventType eventType, T data) {
        List<EventListener<?>> eventListeners = listeners.get(eventType);
        eventListeners.forEach(listener -> ((EventListener<T>) listener).update(data));
    }
}