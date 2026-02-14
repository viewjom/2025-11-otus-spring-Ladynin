package ru.otus.hw.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.controller.rest.BookRestController;
import ru.otus.hw.dto.*;
import ru.otus.hw.services.AuthorServiceImpl;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreServiceImpl;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@DisplayName("Тестирование rest контроллера книг")
@WebMvcTest(BookRestController.class)
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
    void shouldRenderListPageWithCorrectViewAndModelAttributes() throws Exception {
        List<BookDto> expectedBookDtoList = getTestListBookDto();

        when(bookService.findAll())
                .thenReturn(expectedBookDtoList);

        mvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(expectedBookDtoList)));
    }

    @DisplayName("должен сохранять измененную книгу")
    @Test
    void shouldSavePersonAndRedirectToContextPath() throws Exception {
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
    void shouldDeletePersonAndRedirectToContextPath() throws Exception {
        BookDto expectedBookDto = getTestListBookDto().get(0);
        String expectedResult = mapper.writeValueAsString(expectedBookDto);

        mvc.perform(delete("/api/books/" + TEST_ID).contentType(APPLICATION_JSON)
                        .content(expectedResult))
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