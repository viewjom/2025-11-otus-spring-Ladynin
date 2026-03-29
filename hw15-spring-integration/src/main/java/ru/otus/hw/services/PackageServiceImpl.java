package ru.otus.hw.services;


import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.hw.domain.Item;
import ru.otus.hw.domain.Package;

@Service
@Slf4j
public class PackageServiceImpl implements PackageService {

    @Override
    public Package pack(List<Item> items) {
        String list = items.stream().map(s -> s.name()).collect(Collectors.joining(", "));
        log.info("Packing [{}]", list);
        delay();
        return new Package(list);
    }

    private static void delay() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}