package ru.otus.dp.services;

import org.springframework.messaging.MessageHeaders;

public interface FileService {

    void move(MessageHeaders messageHeaders, String reply, String cdrRow);

    void delete();
}
