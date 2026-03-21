package ru.otus.hw.batch;

import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.Chunk;
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

@Configuration
@RequiredArgsConstructor
public class JobConfig<T> {

    public static final String IMPORT_BOOK_JOB_NAME = "importBookJob";

    private static final int CHUNK_SIZE = 5;

    private final JpaAuthorRepository jpaAuthorRepository;

    private final JpaGenreRepository jpaGenreRepository;

    private final JpaBookRepository jpaBookRepository;

    private final JpaCommentRepository jpaCommentRepository;

    private final MongoAuthorRepository mongoAuthorRepository;

    private final MongoGenreRepository mongoGenreRepository;

    private final MongoBookRepository mongoBookRepository;

    private final MongoCommentRepository mongoCommentRepository;

    private final BatchCache cache;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private PlatformTransactionManager platformTransactionManager;

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
    public ItemProcessor<MongoAuthor, JpaAuthor> authorProcessor() {
        return item -> {
            return new JpaAuthor(0L, item.getFullName(), item.getId());
        };
    }

    @Bean
    public ItemProcessor<MongoGenre, JpaGenre> genreProcessor() {
        return item -> {
            return new JpaGenre(0L, item.getName(), item.getId());
        };
    }

    @Bean
    public ItemProcessor<MongoBook, JpaBook> bookProcessor() {
        return item -> {
            JpaAuthor jpaAuthor = (JpaAuthor) cache.get(item.getAuthor().getId());
            JpaGenre jpaGenre = (JpaGenre) cache.get(item.getGenre().getId());
            return new JpaBook(0l, item.getTitle(), jpaAuthor, jpaGenre, item.getId());
        };
    }

    @Bean
    public ItemProcessor<MongoComment, JpaComment> commentProcessor() {
        return item -> {
            JpaBook jpaBook = (JpaBook) cache.get(item.getBook().getId());
            return new JpaComment(0l, item.getText(), jpaBook);
        };
    }

    @Bean
    public RepositoryItemWriter<JpaAuthor> authorWriter() {
        RepositoryItemWriter<JpaAuthor> writer = new RepositoryItemWriter<>();
        writer.setRepository(jpaAuthorRepository);
        writer.setMethodName("save");
        return writer;
    }

    @Bean
    public RepositoryItemWriter<JpaGenre> genreWriter() {
        RepositoryItemWriter<JpaGenre> writer = new RepositoryItemWriter<>();
        writer.setRepository(jpaGenreRepository);
        writer.setMethodName("save");
        return writer;
    }

    @Bean
    public RepositoryItemWriter<JpaBook> bookWriter() {
        RepositoryItemWriter<JpaBook> writer = new RepositoryItemWriter<>();
        writer.setRepository(jpaBookRepository);
        writer.setMethodName("save");
        return writer;
    }

    @Bean
    public RepositoryItemWriter<JpaComment> commentWriter() {
        RepositoryItemWriter<JpaComment> writer = new RepositoryItemWriter<>();
        writer.setRepository(jpaCommentRepository);
        writer.setMethodName("save");
        return writer;
    }

    @Bean
    public Job importFromDatabaseJob(Step transformBookStep,
                                     Step transformAuthorStep,
                                     Step transformGenreStep,
                                     Step transformCommentStep
    ) {
        return new JobBuilder(IMPORT_BOOK_JOB_NAME, jobRepository)
                .incrementer(new RunIdIncrementer())
                .flow(transformAuthorStep)
                .next(transformGenreStep)
                .next(transformBookStep)
                .next(transformCommentStep)
                .end()
                .build();
    }

    @Bean
    public Step transformAuthorStep(RepositoryItemReader<MongoAuthor> reader,
                                    RepositoryItemWriter<JpaAuthor> writer,
                                    ItemProcessor<MongoAuthor, JpaAuthor> itemProcessor
    ) {
        return new StepBuilder("transformAuthorStep", jobRepository)
                .<MongoAuthor, JpaAuthor>chunk(CHUNK_SIZE, platformTransactionManager)
                .reader(reader)
                .processor(itemProcessor)
                .writer(writer)
                .listener(new ItemWriteListener<JpaAuthor>() {
                    public void afterWrite(Chunk<? extends JpaAuthor> items) {
                        items.getItems().forEach(a -> cache.put(a.getMongoId(), (T) a));
                    }
                })
                .build();
    }

    @Bean
    public Step transformGenreStep(RepositoryItemReader<MongoGenre> reader,
                                   RepositoryItemWriter<JpaGenre> writer,
                                   ItemProcessor<MongoGenre, JpaGenre> itemProcessor
    ) {
        return new StepBuilder("transformGenreStep", jobRepository)
                .<MongoGenre, JpaGenre>chunk(CHUNK_SIZE, platformTransactionManager)
                .reader(reader)
                .processor(itemProcessor)
                .writer(writer)
                .listener(new ItemWriteListener<JpaGenre>() {
                    public void afterWrite(Chunk<? extends JpaGenre> items) {
                        items.getItems().forEach(g -> cache.put(g.getMongoId(), (T) g));
                    }
                })
                .build();
    }

    @Bean
    public Step transformBookStep(RepositoryItemReader<MongoBook> reader,
                                  RepositoryItemWriter<JpaBook> writer,
                                  ItemProcessor<MongoBook, JpaBook> itemProcessor
    ) {
        return new StepBuilder("transformBookStep", jobRepository)
                .<MongoBook, JpaBook>chunk(CHUNK_SIZE, platformTransactionManager)
                .reader(reader)
                .processor(itemProcessor)
                .writer(writer)
                .listener(new ItemWriteListener<JpaBook>() {
                    public void afterWrite(Chunk<? extends JpaBook> items) {
                        items.getItems().forEach(b -> cache.put(b.getMongoId(), (T) b));
                    }
                })
                .build();
    }

    @Bean
    public Step transformCommentStep(RepositoryItemReader<MongoComment> reader,
                                     RepositoryItemWriter<JpaComment> writer,
                                     ItemProcessor<MongoComment, JpaComment> itemProcessor) {
        return new StepBuilder("transformCommentStep", jobRepository)
                .<MongoComment, JpaComment>chunk(CHUNK_SIZE, platformTransactionManager)
                .reader(reader)
                .processor(itemProcessor)
                .writer(writer)
                .build();
    }
}