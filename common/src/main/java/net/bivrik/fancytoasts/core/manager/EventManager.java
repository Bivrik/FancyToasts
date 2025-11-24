package net.bivrik.fancytoasts.core.manager;

import net.bivrik.fancytoasts.core.IManager;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

public class EventManager implements IManager {
    private final Map<Class<?>, List<Consumer<?>>> listeners = new ConcurrentHashMap<>();

    public <T> void subscribe(Class<T> eventClass, Consumer<T> listener) {
        listeners.computeIfAbsent(eventClass, k -> new CopyOnWriteArrayList<>()).add(listener);
    }

    public <T> void unsubscribe(Class<T> eventClass, Consumer<T> listener) {
        List<Consumer<?>> listeners = this.listeners.get(eventClass);
        if (listeners != null) {
            listeners.remove(listener);
        }
    }

    public <T> void changed(T event) {
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
