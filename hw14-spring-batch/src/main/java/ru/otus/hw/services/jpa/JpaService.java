package ru.otus.hw.services.jpa;

import ru.otus.hw.models.jpa.JpaComment;
import ru.otus.hw.models.mongo.MongoComment;

public interface JpaService {

    JpaComment prepareJpaComment(MongoComment comment);
}
