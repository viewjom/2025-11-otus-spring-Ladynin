package ru.otus.dp.services;

import java.util.Collection;
import ru.otus.dp.domain.Cdr;

public interface CdrService {

    Cdr parseCdr(String value);
}
