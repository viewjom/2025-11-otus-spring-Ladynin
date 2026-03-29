package ru.otus.hw.services;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Service;
import ru.otus.hw.domain.Item;
import ru.otus.hw.domain.OrderItem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {
    private static final String[] ITEM = {"scotch", "notebook", "coffee", "washing powder"
            , "headphones", "paper", "scissors"};

    private final OrderGateway order;

    public OrderServiceImpl(OrderGateway cafe) {
        this.order = cafe;
    }

    @Override
    public void startGenerateOrdersLoop() {
        ForkJoinPool pool = ForkJoinPool.commonPool();
        for (int i = 0; i < 10; i++) {
            int num = i + 1;
            pool.execute(() -> {
                Collection<OrderItem> items = generateOrderItems();
                log.info("{}, New orderItems: {}", num,
                        items.stream().map(OrderItem::itemName)
                                .collect(Collectors.joining(",")));
                Collection<Item> item = order.process(items);
                log.info("{}, Ready item: {}", num, item.stream()
                        .map(Item::name)
                        .collect(Collectors.joining(",")));
            });
            delay();
        }
    }

    private static OrderItem generateOrderItem() {
        return new OrderItem(ITEM[RandomUtils.nextInt(0, ITEM.length)]);
    }

    private static Collection<OrderItem> generateOrderItems() {
        List<OrderItem> items = new ArrayList<>();
        for (int i = 0; i < RandomUtils.nextInt(1, 5); ++i) {
            items.add(generateOrderItem());
        }
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

