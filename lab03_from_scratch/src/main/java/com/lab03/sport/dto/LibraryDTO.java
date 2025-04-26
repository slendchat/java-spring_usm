// src/main/java/com/lab03/sport/dto/LibraryDTO.java
package com.lab03.sport.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;
import java.util.Collections;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class LibraryDTO {
    private Long libId;

    @NotBlank(message = "Название библиотеки не должно быть пустым")
    @Size(min = 1, max = 254, message = "Длина названия библиотеки: от 1 до 254 символов")
    private String libName;

    private List<BookDTO> books;
}
