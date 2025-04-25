package com.example.library.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookDTO {
    private Long id;
    @NotBlank(message = "Title must not be blank")
    private String title;

    @NotNull(message = "authorId is required")
    private Long authorId;

    private String authorName;

    @NotNull(message = "publisherId is required")
    private Long publisherId;

    private String publisherName;

    @NotEmpty(message = "categoryIds must contain at least one entry")
    private Set<Long> categoryIds;

    private Set<String> categoryNames;
}
