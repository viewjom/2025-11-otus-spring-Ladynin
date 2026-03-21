package ru.otus.hw.batch;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.springframework.stereotype.Component;

@Component
public class BatchCache<K, T> {
    private final ConcurrentMap<K, T> cache = new ConcurrentHashMap<>();

    public T get(K key) {
        return cache.get(key);
    }

    public void put(K key, T value) {
        cache.put(key, value);
    }
}