package ru.otus.hw.services;

import ru.otus.hw.domain.Food;
import ru.otus.hw.domain.OrderItem;

public interface KitchenService {
    Food cook(OrderItem orderItem);
}


