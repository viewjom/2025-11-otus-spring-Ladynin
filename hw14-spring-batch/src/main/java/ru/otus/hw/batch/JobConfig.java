package ru.otus.hw.batch;

import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.PlatformTransactionManager;
import ru.otus.hw.models.jpa.JpaAuthor;
import ru.otus.hw.models.jpa.JpaBook;
import ru.otus.hw.models.jpa.JpaComment;
import ru.otus.hw.models.jpa.JpaGenre;
import ru.otus.hw.models.mongo.MongoBook;
import ru.otus.hw.models.mongo.MongoAuthor;
import ru.otus.hw.models.mongo.MongoComment;
import ru.otus.hw.models.mongo.MongoGenre;
import ru.otus.hw.repositories.jpa.JpaAuthorRepository;
import ru.otus.hw.repositories.jpa.JpaBookRepository;
import ru.otus.hw.repositories.jpa.JpaCommentRepository;
import ru.otus.hw.repositories.jpa.JpaGenreRepository;
import ru.otus.hw.repositories.mongo.MongoBookRepository;
import ru.otus.hw.repositories.mongo.MongoCommentRepository;
import ru.otus.hw.repositories.mongo.MongoGenreRepository;
import ru.otus.hw.repositories.mongo.MongoAuthorRepository;
import ru.otus.hw.services.jpa.JpaService;

@Configuration
@RequiredArgsConstructor
public class JobConfig {

    public static final String IMPORT_BOOK_JOB_NAME = "importBookJob";

    private static final int CHUNK_SIZE = 5;

    private final JpaAuthorRepository jpaAuthorRepository;

    private final JpaGenreRepository jpaGenreRepository;

    private final JpaBookRepository jpaBookRepository;

    private final JpaCommentRepository jpaCommentRepository;

    private final JpaService jpaService;

    private final MongoAuthorRepository mongoAuthorRepository;

    private final MongoGenreRepository mongoGenreRepository;

    private final MongoBookRepository mongoBookRepository;

    private final MongoCommentRepository mongoCommentRepository;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private PlatformTransactionManager platformTransactionManager;

    @StepScope
    @Bean
    public RepositoryItemReader<MongoAuthor> authorReader() {
        Map<String, Sort.Direction> sorts = new HashMap<>();
        sorts.put("fullName", Sort.Direction.ASC);

        RepositoryItemReader<MongoAuthor> reader = new RepositoryItemReader<>();
        reader.setName("authorReader");
        reader.setRepository(mongoAuthorRepository);
        reader.setMethodName("findAll");
        reader.setPageSize(CHUNK_SIZE);
        reader.setSort(sorts);
        return reader;

    }

    @StepScope
    @Bean
    public RepositoryItemReader<MongoGenre> genreReader() {
        Map<String, Sort.Direction> sorts = new HashMap<>();
        sorts.put("nane", Sort.Direction.ASC);

        RepositoryItemReader<MongoGenre> reader = new RepositoryItemReader<>();
        reader.setName("genreReader");
        reader.setRepository(mongoGenreRepository);
        reader.setMethodName("findAll");
        reader.setPageSize(CHUNK_SIZE);
        reader.setSort(sorts);
        return reader;
    }

    @StepScope
    @Bean
    public RepositoryItemReader<MongoBook> bookReader() {
        Map<String, Sort.Direction> sorts = new HashMap<>();
        sorts.put("title", Sort.Direction.ASC);

        RepositoryItemReader<MongoBook> reader = new RepositoryItemReader<>();
        reader.setName("bookReader");
        reader.setRepository(mongoBookRepository);
        reader.setMethodName("findAll");
        reader.setPageSize(CHUNK_SIZE);
        reader.setSort(sorts);
        return reader;
    }

    @StepScope
    @Bean
    public RepositoryItemReader<MongoComment> commentItemReader() {
        Map<String, Sort.Direction> sorts = new HashMap<>();
        sorts.put("id", Sort.Direction.ASC);

        RepositoryItemReader<MongoComment> reader = new RepositoryItemReader<>();
        reader.setName("commentReader");
        reader.setRepository(mongoCommentRepository);
        reader.setMethodName("findAll");
        reader.setPageSize(CHUNK_SIZE);
        reader.setSort(sorts);
        return reader;
    }


    @Bean
    public AuthorProcessor authorProcessor() {
        return new AuthorProcessor();
    }

    @Bean
    public GenreProcessor genreProcessor() {
        return new GenreProcessor();
    }

    @Bean
    public BookProcessor bookProcessor() {
        return new BookProcessor();
    }

    @StepScope
    @Bean
    public RepositoryItemWriter<JpaAuthor> authorWriter() {
        RepositoryItemWriter<JpaAuthor> writer = new RepositoryItemWriter<>();
        writer.setRepository(jpaAuthorRepository);
        writer.setMethodName("save");
        return writer;
    }

    @StepScope
    @Bean
    public RepositoryItemWriter<JpaGenre> genreWriter() {
        RepositoryItemWriter<JpaGenre> writer = new RepositoryItemWriter<>();
        writer.setRepository(jpaGenreRepository);
        writer.setMethodName("save");
        return writer;
    }

    @StepScope
    @Bean
    public RepositoryItemWriter<JpaBook> bookWriter() {
        RepositoryItemWriter<JpaBook> writer = new RepositoryItemWriter<>();
        writer.setRepository(jpaBookRepository);
        writer.setMethodName("save");
        return writer;
    }

    @StepScope
    @Bean
    public RepositoryItemWriter<JpaComment> commentWriter() {
        RepositoryItemWriter<JpaComment> writer = new RepositoryItemWriter<>();
        writer.setRepository(jpaCommentRepository);
        writer.setMethodName("save");
        return writer;
    }

    @Bean
    public Job importFromDatabaseJob(Step transformBookStep,
                                     Step transformCommentStep
    ) {
        return new JobBuilder(IMPORT_BOOK_JOB_NAME, jobRepository)
                .incrementer(new RunIdIncrementer())
                .flow(transformBookStep)
                .next(transformCommentStep)
                .end()
                .build();
    }

    @Bean
    public Step transformBookStep(RepositoryItemReader<MongoBook> reader,
                                  RepositoryItemWriter<JpaBook> writer,
                                  BookProcessor itemProcessor) {
        return new StepBuilder("transformBookStep", jobRepository)
                .<MongoBook, JpaBook>chunk(CHUNK_SIZE, platformTransactionManager)
                .reader(reader)
                .processor(itemProcessor)
                .writer(writer)
                .build();
    }

    @Bean
    public Step transformCommentStep(RepositoryItemReader<MongoComment> reader,
                                     RepositoryItemWriter<JpaComment> writer) {
        return new StepBuilder("transformCommentStep", jobRepository)
                .<MongoComment, JpaComment>chunk(CHUNK_SIZE, platformTransactionManager)
                .reader(reader)
                .processor((ItemProcessor<MongoComment, JpaComment>) jpaService::prepareJpaComment)
                .writer(writer)
                .build();
    }
}