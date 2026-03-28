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
        if (authorService.getErrorAuthors().size() == 0) {
            return Health.up()
                    .withDetail("message", "Есть книги всех авторов")
                    .build();
        } else {
            return Health.down()
                    .withDetail("message", "Отсутствуют книги для авторов: "
                            + authorService.getErrorAuthors())
                    .build();
        }
    }
}
