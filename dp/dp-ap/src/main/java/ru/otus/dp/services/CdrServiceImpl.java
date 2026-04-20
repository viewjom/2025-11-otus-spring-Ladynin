package ru.otus.dp.services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.otus.dp.domain.Cdr;
import java.util.Date;

@Service
public class CdrServiceImpl implements CdrService {

    @Value("${app.input.format-date}")
    private String formatDate ;

    @Override
    public Cdr parseCdr(String row) {
        String[] cdrElements = row.split(";");

        SimpleDateFormat formatter = new SimpleDateFormat(formatDate);
        Date date;
        try {
            date = formatter.parse(cdrElements[0]);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        Cdr cdr = new Cdr(date,
                cdrElements[1],
                cdrElements[2],
                Long.parseLong(cdrElements[3]));
        return cdr;
    }
}
