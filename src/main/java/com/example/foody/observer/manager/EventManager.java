package com.example.foody.observer.manager;

import com.example.foody.observer.listener.EventListener;
import com.example.foody.utils.enums.EventType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.*;

@Getter
@Setter
@Component
public class EventManager {
    private final Map<EventType, List<EventListener<?>>> listeners = new EnumMap<>(EventType.class);

    public EventManager() {
        Arrays.stream(EventType.values()).forEach(eventType ->
                listeners.put(eventType, new ArrayList<>())
        );
    }

    // T - type of data that will be sent to listeners
    public <T> void subscribe(EventType eventType, EventListener<T> listener) {
        List<EventListener<?>> eventListeners = listeners.get(eventType);
        eventListeners.add(listener);
    }

    public <T> void unsubscribe(EventType eventType, EventListener<T> eventListener) {
        List<EventListener<?>> eventListeners = listeners.get(eventType);
        eventListeners.remove(eventListener);
    }

    public <T> void notifyListeners(EventType eventType, T data) {
        List<EventListener<?>> eventListeners = listeners.get(eventType);
        eventListeners.forEach(listener -> ((EventListener<T>) listener).update(data));
    }
}