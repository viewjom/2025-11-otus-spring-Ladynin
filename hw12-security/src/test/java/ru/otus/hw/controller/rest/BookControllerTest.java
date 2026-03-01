package ru.otus.hw.controller.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.services.AuthorServiceImpl;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreServiceImpl;

import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@DisplayName("Тестирование rest контроллера книг")
@WebMvcTest(controllers = BookRestController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
class BookControllerTest {

    private static final long TEST_ID = 1L;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockitoBean
    private BookService bookService;

    @MockitoBean
    private AuthorServiceImpl authorService;

    @MockitoBean
    private GenreServiceImpl genreService;


    @DisplayName("должен загружать список всех книг")
    @Test
    void shouldReturnCorrectBooksList() throws Exception {
        List<BookDto> expectedBookDtoList = getTestListBookDto();

        when(bookService.findAll())
                .thenReturn(expectedBookDtoList);

        mvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(expectedBookDtoList)));
    }

    @DisplayName("должен загружать книгу по id")
    @Test
    void shouldRenderListPageWithCorrectViewAndModelAttributes() throws Exception {
        BookDto expectedBookDto = getTestListBookDto()
                .stream().filter(v -> v.getId() == TEST_ID).toList().get(0);

        when(bookService.findById(TEST_ID))
                .thenReturn(expectedBookDto);

        mvc.perform(get("/api/books/" + TEST_ID))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(expectedBookDto)));
    }

    @DisplayName("должен сохранять измененную книгу")
    @Test
    void shouldCorrectSaveNewBook() throws Exception {
        BookDto expectedBookDto = getTestListBookDto().get(0);
        when(bookService.update(expectedBookDto.getId(),
                expectedBookDto.getTitle(),
                expectedBookDto.getAuthorDto().getId(),
                expectedBookDto.getGenreDto().getId())).thenReturn(expectedBookDto);
        String expectedResult = mapper.writeValueAsString(expectedBookDto);

        mvc.perform(post("/api/books").contentType(APPLICATION_JSON)
                        .content(expectedResult))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResult));
    }

    @DisplayName("должен удалять книгу по id ")
    @Test
    void shouldDeleteBook() throws Exception {
        mvc.perform(delete("/api/books/" + TEST_ID).contentType(APPLICATION_JSON))
                .andExpect(status().isOk());
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