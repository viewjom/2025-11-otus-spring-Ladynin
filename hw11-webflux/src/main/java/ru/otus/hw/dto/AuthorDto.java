package ru.otus.hw.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
public class AuthorDto {

    private String id;

    private String fullName;
}
