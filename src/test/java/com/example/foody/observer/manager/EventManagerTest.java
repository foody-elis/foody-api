package com.example.foody.observer.manager;

import com.example.foody.observer.listener.EventListener;
import com.example.foody.utils.enums.EventType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link EventManager} class using mock services.
 */
public class EventManagerTest {

    private EventManager eventManager;

    @BeforeEach
    void setUp() {
        eventManager = new EventManager();
    }

    @Test
    void subscribeWhenListenerIsAddedSuccessfully() {
        // Arrange
        EventType eventType = EventType.ORDER_CREATED;
        EventListener<String> listener = mock(EventListener.class);

        // Act
        eventManager.subscribe(eventType, listener);

        // Assert
        assertEquals(1, eventManager.getListeners().get(eventType).size());
        assertTrue(eventManager.getListeners().get(eventType).contains(listener));
    }

    @Test
    void unsubscribeWhenListenerIsRemovedSuccessfully() {
        // Arrange
        EventType eventType = EventType.ORDER_CREATED;
        EventListener<String> listener = mock(EventListener.class);
        eventManager.subscribe(eventType, listener);

        // Act
        eventManager.unsubscribe(eventType, listener);

        // Assert
        assertEquals(0, eventManager.getListeners().get(eventType).size());
        assertFalse(eventManager.getListeners().get(eventType).contains(listener));
    }

    @Test
    void notifyListenersWhenAllListenersAreNotified() {
        // Arrange
        EventType eventType = EventType.ORDER_CREATED;
        EventListener<String> listener1 = mock(EventListener.class);
        EventListener<String> listener2 = mock(EventListener.class);
        eventManager.subscribe(eventType, listener1);
        eventManager.subscribe(eventType, listener2);

        String data = "Order123";

        // Act
        eventManager.notifyListeners(eventType, data);

        // Assert
        verify(listener1, times(1)).update(data);
        verify(listener2, times(1)).update(data);
    }

    @Test
    void notifyListenersWhenNoListenersExistDoesNotThrowException() {
        // Arrange
        EventType eventType = EventType.ORDER_CREATED;
        String data = "Order123";

        // Act & Assert
        assertDoesNotThrow(() -> eventManager.notifyListeners(eventType, data));
    }
}