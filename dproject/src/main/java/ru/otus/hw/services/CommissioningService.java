package ru.otus.hw.services;

import ru.otus.hw.domain.Item;
import ru.otus.hw.domain.DispItem;

public interface CommissioningService {

    Item sort(DispItem orderItem);
}
