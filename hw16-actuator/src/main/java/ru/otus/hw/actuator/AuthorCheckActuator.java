package ru.otus.hw.actuator;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import ru.otus.hw.services.AuthorService;

@Component
@RequiredArgsConstructor
public class AuthorCheckActuator implements HealthIndicator {

    private final AuthorService authorService;

    @Override
    public Health health() {
        int count = authorService.findErrorAuthors();
        if (count == 0) {
            return Health.up()
                    .withDetail("message", "Есть книги всех авторов")
                    .build();
        } else {
            return Health.down()
                    .withDetail("message", String.format("Отсутствуют книги для %d %s",
                            count,
                            count == 1 ? "автора" : "авторов"))
                    .build();
        }
    }
}
