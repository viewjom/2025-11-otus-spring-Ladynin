package ru.otus.hw.services;

import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.hw.domain.DispItem;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@Slf4j
public class DispServiceImpl implements DispService {

    public Collection<DispItem> findOrderItems(String row) {
        String[] itemElements = row.split(";");
        List<DispItem> items = new ArrayList<>();
        for (String i:itemElements) {
            items.add(new DispItem(i));
        }
        log.info("Disp is being processed: {}", items.stream()
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

