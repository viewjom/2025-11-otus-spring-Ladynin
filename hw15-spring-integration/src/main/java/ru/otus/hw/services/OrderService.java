package ru.otus.hw.services;

import java.util.Collection;
import ru.otus.hw.domain.OrderItem;

public interface OrderService {

    Collection<OrderItem> findOrderItems(String row);
}