// src/main/java/com/lab03/sport/dto/BookDTO.java
package com.lab03.sport.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookDTO {

    /**
     * При создании (POST) будет null,
     * при обновлении (PUT) — должны передать существующий ID.
     */
    private Long bookId;

    @NotBlank(message = "Название книги не должно быть пустым")
    @Size(min = 1, max = 254, message = "Длина названия: от 1 до 254 символов")
    private String bookName;

    @NotNull(message = "Автор обязателен")
    @Valid
    private AuthorDTO author;

    @NotNull(message = "Издатель обязателен")
    @Valid
    private PublisherDTO publisher;

    @NotNull(message = "Категория обязательна")
    @Valid
    private CategoryDTO category;

    @NotNull(message = "Библиотека обязательна")
    @Valid
    private LibraryDTO library;
}
