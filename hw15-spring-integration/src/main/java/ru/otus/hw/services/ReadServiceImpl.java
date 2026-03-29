package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.domain.Call;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@RequiredArgsConstructor
public class ReadServiceImpl implements ReadService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReadServiceImpl.class);


    @Override
    public void create(String row) {
        String[] splittedRow = row.split(";");
        Call call = new Call(splittedRow[0], splittedRow[1], Long.parseLong(splittedRow[2]));
        LOGGER.info("Create Call: {}", call.toString());
       // return call;
    }
}
