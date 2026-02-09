package ru.otus.hw.controller;

import java.util.Arrays;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.converters.AuthorDtoConverter;
import ru.otus.hw.converters.BookDtoConverter;
import ru.otus.hw.converters.GenreDtoConverter;
import ru.otus.hw.dto.*;
import ru.otus.hw.services.AuthorServiceImpl;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreServiceImpl;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@DisplayName("Тестирование контроллера книг")
@WebMvcTest(BookController.class)
@Import({BookDtoConverter.class, AuthorDtoConverter.class, GenreDtoConverter.class})
class BookControllerTest {

    private static final long TEST_ID = 1L;

    @Autowired
    private MockMvc mvc;

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
        given(bookService.findAll())
                .willReturn(expectedBookDtoList);

        mvc.perform(get("/"))
                .andExpect(view().name("bookList"))
                .andExpect(model().attribute("books", expectedBookDtoList));
    }


    @DisplayName("должен загружать книгу по id")
    @Test
    void shouldRenderEditPageWithCorrectViewAndModelAttributes() throws Exception {
        BookDto expectedBookDto = getTestListBookDto().get(0);
        given(bookService.findById(TEST_ID))
                .willReturn(expectedBookDto);

        mvc.perform(get("/bookEdit").param("id", String.valueOf(TEST_ID)))
                .andExpect(view().name("bookEdit"))
                .andExpect(model().attribute("book", expectedBookDto));
    }

    @DisplayName("должен сохранять измененную книгу")
    @Test
    void shouldSavePersonAndRedirectToContextPath() throws Exception {
        BookDto expectedBookDto = getTestListBookDto().get(0);
        when(bookService.findById(TEST_ID)).thenReturn(expectedBookDto);

        mvc.perform(
                        post("/bookEdit")
                                .param("id", String.valueOf(TEST_ID))
                                .param("title", "Test")
                                .param("authorDto.id", String.valueOf(TEST_ID))
                                .param("authorDto.fullName", "Test")
                                .param("genreDto.id", String.valueOf(TEST_ID))
                                .param("genreDto.name", "Test")
                )
                .andExpect(view().name("redirect:/"));
        verify(bookService, times(1)).update(TEST_ID, "Test", TEST_ID, TEST_ID);
    }

    @DisplayName("должен удалять книгу по id ")
    @Test
    void shouldDeletePersonAndRedirectToContextPath() throws Exception {
        BookDto expectedBookDto = getTestListBookDto().get(0);
        when(bookService.findById(TEST_ID)).thenReturn(expectedBookDto);

        mvc.perform(post("/delete")
                        .param("id", String.valueOf(TEST_ID)))
                .andExpect(view().name("redirect:/"));
        verify(bookService, times(1)).deleteById(TEST_ID);
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