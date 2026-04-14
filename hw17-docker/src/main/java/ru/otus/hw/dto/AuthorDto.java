package ru.otus.hw.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;

@Getter
@Setter
@AllArgsConstructor
public class AuthorDto {

    private Long id;

    private String fullName;
}
