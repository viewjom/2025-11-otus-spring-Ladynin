package ru.otus.hw.dao;

import com.opencsv.bean.CsvToBeanBuilder;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.dto.QuestionDto;
import ru.otus.hw.domain.Question;

import java.util.List;
import ru.otus.hw.exceptions.QuestionReadException;

import static java.util.Objects.isNull;

@RequiredArgsConstructor
public class CsvQuestionDao implements QuestionDao {
    private final TestFileNameProvider fileNameProvider;

    @Override
    public List<Question> findAll() {
        try {
            InputStream inputStream = Question.class.getClassLoader()
                    .getResourceAsStream(fileNameProvider.getTestFileName());
            if (isNull(inputStream)) {
                throw new QuestionReadException("Questions file input stream is null");
            }
            try (InputStreamReader reader = new InputStreamReader(inputStream)) {
                return new CsvToBeanBuilder<QuestionDto>(reader)
                        .withType(QuestionDto.class)
                        .withSkipLines(1)
                        .withSeparator(';')
                        .build()
                        .parse()
                        .stream()
                        .map(QuestionDto::toDomainObject)
                        .collect(Collectors.toList());
            }
        } catch (Exception e) {
            throw new QuestionReadException("Error during reading questions");
        }
    }
}