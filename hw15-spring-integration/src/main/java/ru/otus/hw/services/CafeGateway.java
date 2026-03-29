package ru.otus.hw.services;

import java.util.Collection;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import ru.otus.hw.domain.Food;
import ru.otus.hw.domain.OrderItem;

@MessagingGateway
public interface CafeGateway {

    @Gateway(requestChannel = "itemsChannel", replyChannel = "foodChannel")
    Collection<Food> process(Collection<OrderItem> orderItem);
}


