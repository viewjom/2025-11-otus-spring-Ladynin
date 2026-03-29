package ru.otus.hw.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.otus.hw.domain.Call;
import ru.otus.hw.domain.Traffic;

@Service
public class TrafficServiceImpl implements TrafficService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TrafficServiceImpl.class);
    @Override
    public Traffic calculateCall(String row) {
        String[] splittedRow = row.split(";");
        String type;
        if (splittedRow[1].startsWith("+7495")) {
            type = "Local";
        } else if (splittedRow[1].startsWith("+7")) {
            type = "Long-distance";
        } else {
            type = "International";
        }
        delay();
        LOGGER.info("Calculate {} Call: {}", type, row);
        return new Traffic(type);
    }

    @Override
    public Traffic calculateNetflow(String row) {
        return null;
    }

    private static void delay() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
