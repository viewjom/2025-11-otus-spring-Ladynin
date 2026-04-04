package ru.otus.hw.services;

import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.hw.domain.Item;
import ru.otus.hw.domain.Sort;

@Service
@Slf4j
public class SortingServiceImpl implements SortingService {

    @Override
    public Sort pack(List<Item> items) {
        String list = items.stream().map(s -> s.name()).collect(Collectors.joining(", "));
        log.info("Sorting [{}]", list);
        delay();
        log.info("Sorting [{}] done", list);
        return new Sort(list);
    }

    private static void delay() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}