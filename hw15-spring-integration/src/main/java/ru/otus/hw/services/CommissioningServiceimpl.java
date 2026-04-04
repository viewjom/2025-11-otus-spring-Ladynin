package ru.otus.hw.services;

import org.springframework.stereotype.Service;
import ru.otus.hw.domain.Item;
import ru.otus.hw.domain.DispItem;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CommissioningServiceimpl implements CommissioningService {

    @Override
    public Item sort(DispItem orderItem) {
        log.info("Commissioning {}", orderItem.itemName());
        delay();
        log.info("Commissioning {} done", orderItem.itemName());
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
