package ru.otus.hw.services;

import java.util.Collection;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;

@MessagingGateway
public interface OrderGateway {

  //  @Gateway(requestChannel = "itemsChannel", replyChannel = "foodChannel")
  //  Collection<Food> process(Collection<OrderItem> orderItem);
}
