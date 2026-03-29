package ru.otus.hw.services;
/*

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.hw.domain.Food;
import ru.otus.hw.domain.OrderItem;

@Service
@Slf4j
public class KitchenServiceImpl implements KitchenService {

    @Override
    public Food cook(OrderItem orderItem) {
        log.info("Cooking {}", orderItem.itemName());
        delay();
        log.info("Cooking {} done", orderItem.itemName());
        return new Food(orderItem.itemName());
    }

    private static void delay() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
*/