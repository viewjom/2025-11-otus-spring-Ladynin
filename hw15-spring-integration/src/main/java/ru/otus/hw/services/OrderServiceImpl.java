package ru.otus.hw.services;

import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.hw.domain.OrderItem;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    public Collection<OrderItem> findOrderItems(String row) {
        String[] itemElements = row.split(";");
        List<OrderItem> items = new ArrayList<>();
        for (String i:itemElements) {
            items.add(new OrderItem(i));
        }
        log.info("Order is being processed: {}", items.stream()
                .map(i -> i.itemName())
                .collect(Collectors.joining(",")));
        delay();
        return items;
    }

    private void delay() {
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

