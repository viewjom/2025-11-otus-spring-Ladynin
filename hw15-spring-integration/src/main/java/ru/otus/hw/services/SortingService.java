package ru.otus.hw.services;

import ru.otus.hw.domain.Item;
import ru.otus.hw.domain.OrderItem;

public interface SortingService {

    Item sort(OrderItem orderItem);
}
