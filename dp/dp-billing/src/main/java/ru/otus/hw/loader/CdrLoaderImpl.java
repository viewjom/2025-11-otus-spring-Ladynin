package ru.otus.hw.loader;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.otus.hw.exceptions.NotFoundCostException;
import ru.otus.hw.exceptions.NotFoundTelephoneNumberException;
import ru.otus.hw.models.TelephoneNumber;
import ru.otus.hw.services.TrafficDailyService;

@Service
@RequiredArgsConstructor
@Slf4j
public class CdrLoaderImpl implements CdrLoader {

    private @Value("${app.server.separator}") String separator;

    private @Value("${app.server.format-date}") String formatDate;

    private final TrafficDailyService trafficDailyService;

    @Override
    public String load(String message) {
        log.info("Received: {}", message);
        String[] list = message.split(separator);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(formatDate);
        LocalDateTime cdate;
        TelephoneNumber telephoneNumber;
        try {
            cdate = LocalDateTime.parse(list[0], formatter);
        } catch (DateTimeParseException e) {
            return "1";
        }
        try {
            trafficDailyService.create(cdate, list[1], list[2], Double.parseDouble(list[3]));
        } catch (NotFoundTelephoneNumberException e) {
            return "2";
        } catch (NotFoundCostException e) {
            return "2";
        } catch (Exception e) {
            return "3";
        }
        return "0";
    }
}
