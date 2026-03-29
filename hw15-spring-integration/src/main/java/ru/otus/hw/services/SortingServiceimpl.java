package ru.otus.hw.services;

import org.springframework.stereotype.Service;
import ru.otus.hw.domain.Item;
import ru.otus.hw.domain.OrderItem;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SortingServiceimpl implements SortingService {

    @Override
    public Item sort(OrderItem orderItem) {
        log.info("Sorting {}", orderItem.itemName());
           delay();
        log.info("Sorting {} done", orderItem.itemName());
        return new Item(orderItem.itemName());
    }

    private static void delay() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
