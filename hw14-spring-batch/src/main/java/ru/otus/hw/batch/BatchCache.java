package ru.otus.hw.batch;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.springframework.stereotype.Component;

@Component
public class BatchCache<T> {
    private final ConcurrentMap<String, T> cache = new ConcurrentHashMap<>();

    public T get(String key) {
        return cache.get(key);
    }

    public void put(String key, T value) {
        cache.putIfAbsent(key, value);
    }
}