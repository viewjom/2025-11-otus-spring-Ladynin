package ru.otus.hw.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import ru.otus.hw.converter.TestConverter;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Question;

@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final IOService ioService;

    private final QuestionDao questionDao;

    private final TestConverter testConverter;

    @Override
    public void executeTest() {
        ioService.printLine("");
        ioService.printFormattedLine("Please answer the questions below%n");
        List<Question> questions = questionDao.findAll();

        for (Question q : questions) {
            String qts = testConverter.questionToString(q);
            ioService.printLine(qts);
        }
    }
}