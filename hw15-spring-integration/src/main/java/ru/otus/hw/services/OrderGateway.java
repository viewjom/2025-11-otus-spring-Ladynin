package ru.otus.hw.services;

import java.util.Collection;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import ru.otus.hw.domain.Item;
import ru.otus.hw.domain.OrderItem;

@MessagingGateway
public interface OrderGateway {

    @Gateway(requestChannel = "itemsChannel", replyChannel = "packageChannel")
    Collection<Item> process(Collection<OrderItem> orderItem);
}


