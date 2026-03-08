package ru.otus.hw.controller.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.List;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.acls.model.AclService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.otus.hw.config.SecurityConfiguration;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.services.AuthorServiceImpl;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreServiceImpl;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Тестирование rest контроллера книг")
@WebMvcTest({BookRestController.class})
@AutoConfigureMockMvc
@Import({SecurityConfiguration.class})
class BookRestControllerSecureTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private BookService bookService;

    @MockBean
    private AuthorServiceImpl authorService;

    @MockBean
    private GenreServiceImpl genreService;

    @DisplayName("должен загружать книги")
    @Test
    @WithMockUser(username = "user2")
    void shouldGetBookSecureReturnSimple() throws Exception {
        mvc.perform(get("/api/books/1"))
                .andExpect(status().is(200));
    }

    @DisplayName("должен загружать книги")
    @ParameterizedTest
    @CsvSource({
            "/api/books, admin, ROLE_ADMIN, 200",
            "/api/books/1, user1, ROLE_USER1, 200",
            "/api/books/2, user3, ROLE_USER3, 200",
            "/api/books/3, user1, ROLE_USER1, 200"
    })
    void shouldGetBookSecureReturn(String url, String user, String role, int result) throws Exception {
        mvc.perform(get(url)
                .with(user(user).authorities(new SimpleGrantedAuthority(role))))
                .andExpect(status().is(result));
    }

    @DisplayName("должен сохранять измененную книгу")
    @ParameterizedTest
    @CsvSource({
            "/api/books, user1, ROLE_USER1, 403, Test1",
            "/api/books, admin, ROLE_ADMIN, 200, Test2"
    })

    void shouldSaveNewBook(String url, String user, String role, int result, String title) throws Exception {


        BookDto bookDto = getTestListBookDto().get(0);
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
            "/api/books, user2, ROLE_USER2, 200, Test1",
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
            "/api/books/1, user, ROLE_USER, 403",
            "/api/books/2, user, ROLE_USER, 403",
            "/api/books/1, admin, ROLE_ADMIN, 200",
            "/api/books/3, admin, ROLE_ADMIN, 200"
    })
    void shouldDeleteBook(String url, String user, String role, int result) throws Exception {
        BookDto expectedBookDto = getTestListBookDto().get(0);
        String expectedResult = mapper.writeValueAsString(expectedBookDto);

        mvc.perform(delete(url).contentType(APPLICATION_JSON)
                        .with(user(user).authorities(new SimpleGrantedAuthority(role))))
                .andExpect(status().is(result));
    }

    private List<BookDto> getTestListBookDto() {
        BookDto bookDto1 = new BookDto(1L, "BookTitle_1",
                new AuthorDto(1L, "Author_1"),
                new GenreDto(1L, "Genre_1"));
        BookDto bookDto2 = new BookDto(2L, "BookTitle_2",
                new AuthorDto(2L, "Author_2"),
                new GenreDto(2L, "Genre_2"));
        BookDto bookDto3 = new BookDto(3L, "BookTitle_3",
                new AuthorDto(3L, "Author_3"),
                new GenreDto(3L, "Genre_3"));
        return Arrays.asList(bookDto1, bookDto2, bookDto3);
    }
}