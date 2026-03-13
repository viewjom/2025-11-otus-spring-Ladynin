package ru.otus.hw.controller.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.GenreDto;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Тестирование rest контроллера книг")
@SpringBootTest()
@AutoConfigureMockMvc
class BookRestControllerSecureTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @DisplayName("должен загружать книги")
    @ParameterizedTest
    @CsvSource({
            "/api/books, admin, ROLE_ADMIN, 200",
            "/api/books/2, user2, ROLE_AUTHOR2, 200",
            "/api/books/1, user1, ROLE_AUTHOR1, 200",
            "/api/books/3, user3, ROLE_AUTHOR3, 200",
            "/api/books/1, user2, ROLE_AUTHOR2, 403",
            "/api/books/2, user3, ROLE_AUTHOR3, 403",
            "/api/books/3, user1, ROLE_AUTHOR1, 403"
    })
    void shouldGetBookSecureReturn(String url, String user, String role, int result) throws Exception {
        mvc.perform(get(url)
                        .with(user(user).authorities(new SimpleGrantedAuthority(role))))
                .andExpect(status().is(result)).andReturn();
    }

    @DisplayName("должен сохранять измененную книгу")
    @ParameterizedTest
    @CsvSource({
            "/api/books, user1, ROLE_AUTHOR1, 403, Test1",
            "/api/books, admin, ROLE_ADMIN, 200, Test2"
    })
    void shouldSaveNewBook(String url, String user, String role, int result, String title) throws Exception {
        BookDto bookDto = new BookDto(1L, "BookTitle_1",
                new AuthorDto(1L, "Author_1"),
                new GenreDto(1L, "Genre_1"));
        bookDto.setTitle(title);
        bookDto.setAuthorDto(new AuthorDto(2L, "Author_2"));
        bookDto.setGenreDto(new GenreDto(2L, "Genre_2"));
        String content = mapper.writeValueAsString(bookDto);

        mvc.perform(post(url)
                        .with(user(user).authorities(new SimpleGrantedAuthority(role)))
                        .contentType(APPLICATION_JSON)
                        .content(content))
                .andExpect(status().is(result));
    }

    @DisplayName("должен создать новую книгу")
    @ParameterizedTest
    @CsvSource({
            "/api/books, user2, ROLE_AUTHOR2, 403, Test1",
            "/api/books, admin, ROLE_ADMIN, 200, Test2"
    })
    void shouldCreateNewBook(String url, String user, String role, int result, String title) throws Exception {

        BookDto bookDto = new BookDto(0L, "BookTitle_1",
                new AuthorDto(1L, "Author_1"),
                new GenreDto(1L, "Genre_1"));
        String content = mapper.writeValueAsString(bookDto);

        mvc.perform(post(url)
                        .with(user(user).authorities(new SimpleGrantedAuthority(role)))
                        .contentType(APPLICATION_JSON)
                        .content(content))
                .andExpect(status().is(result));
    }

    @DisplayName("должен удалять книгу по id ")
    @ParameterizedTest
    @CsvSource({
            "/api/books/1, user1, ROLE_AUTHOR1, 403",
            "/api/books/2, user2, ROLE_AUTHOR2, 403",
            "/api/books/1, admin, ROLE_ADMIN, 200",
            "/api/books/3, admin, ROLE_ADMIN, 200"
    })
    @Transactional
    void shouldDeleteBook(String url, String user, String role, int result) throws Exception {
        mvc.perform(delete(url).contentType(APPLICATION_JSON)
                        .with(user(user).authorities(new SimpleGrantedAuthority(role))))
                .andExpect(status().is(result));
    }
}