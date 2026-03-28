package ru.otus.hw.services;

import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.converters.AuthorDtoConverter;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.models.Author;
import ru.otus.hw.repositories.AuthorRepository;

import java.util.List;
import java.util.Objects;
import ru.otus.hw.repositories.BookRepository;

@RequiredArgsConstructor
@Service
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    private final BookRepository bookRepository;

    private final AuthorDtoConverter authorConverter;

    @Override
    public List<AuthorDto> findAll() {
        return authorRepository.findAll()
                .stream().map(authorConverter::getDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<AuthorDto> getErrorAuthors() {
        List<Author> authorList = authorRepository.findAll();
        List<AuthorDto> authorDtoList = authorList.stream().map(s -> {
            if (bookRepository.findByAuthor(s).size() == 0) {
                return authorConverter.getDto(s);
            } else {
                return null;
            }
        }) .filter(Objects::nonNull).toList();
        return authorDtoList;
    }
}