package ru.otus.hw.services;

import java.util.List;
import ru.otus.hw.domain.Item;
import ru.otus.hw.domain.Sort;

public interface SortingService {
    Sort pack(List<Item> items);
}


