package ru.otus.hw.services;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.hw.converters.AuthorDtoConverter;
import ru.otus.hw.dto.AuthorDto;
import ru.otus.hw.repositories.AuthorRepository;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    private final AuthorDtoConverter authorConverter;

    @CircuitBreaker(name = "AuthoBreaker", fallbackMethod = "getAuthorListFallback")
    @Override
    public List<AuthorDto> findAll() {
        return authorRepository.findAll()
                .stream().map(authorConverter::getDto)
                .collect(Collectors.toList());
    }

    @Override
    public int findErrorAuthors() {
        return authorRepository.findErrorAuthors();
    }

    public List<AuthorDto> getAuthorListFallback(Exception ex) {
        log.error("Author Fallback:" + ex.getMessage(), ex);
        return null;
    }
}