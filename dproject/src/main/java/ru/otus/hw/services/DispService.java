package ru.otus.hw.services;

import java.util.Collection;
import ru.otus.hw.domain.DispItem;

public interface DispService {

    Collection<DispItem> findOrderItems(String row);
}