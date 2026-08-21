package net.bivrik.fancytoasts.core.manager;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

public class EventManager {
    private final Map<Class<?>, List<Consumer<?>>> listeners = new ConcurrentHashMap<>();

    public <T> void subscribeToEvent(Class<T> eventClass, Consumer<T> listener) {
        listeners.computeIfAbsent(eventClass, k -> new CopyOnWriteArrayList<>()).add(listener);
    }

    public <T> void unsubscribeFromEvent(Class<T> eventClass, Consumer<T> listener) {
        List<Consumer<?>> listeners = this.listeners.get(eventClass);
        if (listeners != null) {
            listeners.remove(listener);
            if (listeners.isEmpty()) {
                this.listeners.remove(eventClass);
            }
        }
    }

    public <T> void sendEvent(T event) {
        List<Consumer<?>> listeners = this.listeners.get(event.getClass());
        if (listeners != null && !listeners.isEmpty()) {
            for (var listener : listeners) {
                @SuppressWarnings("unchecked")
                Consumer<T> result = (Consumer<T>) listener;
                result.accept(event);
            }
        }
    }
}
