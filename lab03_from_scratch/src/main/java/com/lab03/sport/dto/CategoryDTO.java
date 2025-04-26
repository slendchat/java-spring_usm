package com.lab03.sport.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO {
    private Long catId;

    @NotBlank(message = "Название категории не должно быть пустым")
    @Size(min = 1, max = 254, message = "Длина названия категории: от 1 до 254 символов")
    private String catName;
}
