package ru.otus.hw.controller.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.Application;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.GenreDto;
import org.json.JSONObject;
import org.json.JSONException;
import ru.otus.hw.services.BookServiceImpl;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.logout;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Тестирование rest контроллера книг")
@SpringBootTest(classes = Application.class)
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
              "/api/books/1, user1, ROLE_AUTHOR1, 200",
              "/api/books/2, user2, ROLE_AUTHOR2, 200",
              "/api/books/3, user3, ROLE_AUTHOR3, 200",
              "/api/books/1, user2, ROLE_AUTHOR2, 403",
              "/api/books/2, user3, ROLE_AUTHOR3, 403",
              "/api/books/3, user1, ROLE_AUTHOR1, 403"
    })
    void shouldGetBookSecureReturn(String url, String user, String role, int result) throws Exception {
      var res = mvc.perform(get(url)
                        .with(user(user).authorities(new SimpleGrantedAuthority(role))))
                .andExpect(status().is(result)).andReturn();

        res.getRequest().logout();
    }

    @DisplayName("должен сохранять измененную книгу")
    @ParameterizedTest
    @CsvSource({
            "/api/books, user1, ROLE_AUTHOR1, 403, Test1",
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

    @DisplayName("должен создать новую книгу и прочитать пользователями user1 user2 user3")
    @Test
    void shouldCreateNewBookAndRead() throws Exception {

        BookDto bookDto = new BookDto(0L, "BookTitle_1",
                new AuthorDto(1L, "Author_1"),
                new GenreDto(1L, "Genre_1"));
        String content = mapper.writeValueAsString(bookDto);


      var res = mvc.perform(post("/api/books")
                        .with(user("admin").authorities(new SimpleGrantedAuthority("ROLE_ADMIN")))
                        .contentType(APPLICATION_JSON)
                        .content(content))
                .andExpect(status().is(200)).andReturn();

      String jsonString = res.getResponse().getContentAsString();
        JSONObject jsonObject = new JSONObject(jsonString);
        String id = jsonObject.getString("id");

        mvc.perform(get("/api/books/" + id)
                        .with(user("user1").authorities(new SimpleGrantedAuthority("ROLE_AUTHOR1"))))
                .andExpect(status().is(200));

        mvc.perform(get("/api/books/" + id)
                        .with(user("user2").authorities(new SimpleGrantedAuthority("ROLE_AUTHOR2"))))
                .andExpect(status().is(403));

        mvc.perform(get("/api/books/" + id)
                        .with(user("user3").authorities(new SimpleGrantedAuthority("ROLE_AUTHOR3"))))
                .andExpect(status().is(403));
    }

    @DisplayName("должен удалять книгу по id ")
    @ParameterizedTest
    @CsvSource({
            "/api/books/1, user1, ROLE_AUTHOR1, 403",
            "/api/books/2, user2, ROLE_AUTHOR2, 403",
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