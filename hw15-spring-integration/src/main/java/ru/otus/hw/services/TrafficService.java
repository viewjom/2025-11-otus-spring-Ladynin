package ru.otus.hw.services;


import ru.otus.hw.domain.Traffic;

public interface TrafficService {
    Traffic calculateCall(String row);

    Traffic calculateNetflow(String row);
}
