package com.example.library.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PublisherDTO {

    private Long id;

    @NotBlank(message = "Publisher name must not be blank")
    private String name;

    @NotEmpty(message = "bookIds must contain at least one entry")
    private Set<Long> bookIds;

    private Set<BookDTO> books;
}
