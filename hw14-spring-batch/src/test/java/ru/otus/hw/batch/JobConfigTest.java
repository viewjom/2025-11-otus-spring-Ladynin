package ru.otus.hw.batch;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.hw.models.jpa.JpaAuthor;
import ru.otus.hw.models.jpa.JpaBook;
import ru.otus.hw.models.jpa.JpaComment;
import ru.otus.hw.models.jpa.JpaGenre;
import ru.otus.hw.repositories.jpa.JpaAuthorRepository;
import ru.otus.hw.repositories.jpa.JpaBookRepository;
import ru.otus.hw.repositories.jpa.JpaCommentRepository;
import ru.otus.hw.repositories.jpa.JpaGenreRepository;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatList;
import static ru.otus.hw.batch.JobConfig.IMPORT_BOOK_JOB_NAME;


@SpringBootTest
@AutoConfigureDataMongo
@SpringBatchTest
class JobConfigTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private JobRepositoryTestUtils jobRepositoryTestUtils;

    @Autowired
    private JpaBookRepository jpaBookRepository;

    @Autowired
    private JpaAuthorRepository jpaAuthorRepository;

    @Autowired
    private JpaGenreRepository jpaGenreRepository;

    @Autowired
    private JpaCommentRepository jpaCommentRepository;

    @BeforeEach
    void setUp() {
        jobRepositoryTestUtils.removeJobExecutions();
    }

    @Test
    @DisplayName("Тест миграции данных их MongoDB в N2")
    void importFromDatabaseJob() throws Exception {
        Job job = jobLauncherTestUtils.getJob();
        assertThat(job).isNotNull()
                .extracting(Job::getName)
                .isEqualTo(IMPORT_BOOK_JOB_NAME);

        JobExecution jobExecution = jobLauncherTestUtils.launchJob();
        assertThat(jobExecution.getExitStatus().getExitCode()).isEqualTo("COMPLETED");

        assertThatList(jpaAuthorRepository.findAll())
                .isNotEmpty()
                .usingRecursiveComparison()
                .isEqualTo(getExpectedAuthorList());

        assertThatList(jpaGenreRepository.findAll())
                .isNotEmpty()
                .usingRecursiveComparison()
                .isEqualTo(getExpectedGenreList());


        assertThat(jpaBookRepository.findAll())
                .isNotEmpty()
                .usingRecursiveComparison()
                .isEqualTo(getExpectedBookList());

        assertThatList(jpaCommentRepository.findAll())
                .isNotEmpty()
                .usingRecursiveComparison()
                .isEqualTo(getExpectedCommentList());
    }

    private List<JpaAuthor> getExpectedAuthorList() {
        return List.of(new JpaAuthor(1L, "Author_1"),
                new JpaAuthor(2L, "Author_2"),
                new JpaAuthor(3L, "Author_3"));
    }

    private List<JpaGenre> getExpectedGenreList() {
        return List.of(new JpaGenre(1L, "Genre_1"),
                new JpaGenre(2L, "Genre_2"),
                new JpaGenre(3L, "Genre_3"));
    }

    private List<JpaBook> getExpectedBookList() {
        JpaBook book1 = new JpaBook(1L, "BookTitle_1",
                new JpaAuthor(1L, "Author_1"),
                new JpaGenre(1L, "Genre_1"));
        JpaBook book2 = new JpaBook(2L, "BookTitle_2",
                new JpaAuthor(2L, "Author_2"),
                new JpaGenre(2L, "Genre_2"));
        JpaBook book3 = new JpaBook(3L, "BookTitle_3",
                new JpaAuthor(3L, "Author_3"),
                new JpaGenre(3L, "Genre_3"));
        return List.of(book1, book2, book3);
    }

    private List<JpaComment> getExpectedCommentList() {
        List<JpaBook> bookList = getExpectedBookList();
        return List.of(new JpaComment(1L, "Хорошая книга", bookList.get(0)),
                new JpaComment(2L, "Очень хорошая книга", bookList.get(0)),
                new JpaComment(3L, "Нормальная книга", bookList.get(1)),
                new JpaComment(4L, "Прекрасная книга", bookList.get(1)),
                new JpaComment(5L, "Великолепная книга", bookList.get(2)),
                new JpaComment(6L, "Крутая книга", bookList.get(2)));
    }
}