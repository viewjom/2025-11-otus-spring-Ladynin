package ru.otus.hw.services;

import java.util.List;
import ru.otus.hw.domain.Item;
import ru.otus.hw.domain.Package;

public interface PackageService {
    Package pack(List<Item> items);
}


