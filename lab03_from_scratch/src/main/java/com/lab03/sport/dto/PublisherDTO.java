package com.lab03.sport.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PublisherDTO {
    private Long pubId;

    @NotBlank(message = "Название издательства не должно быть пустым")
    @Size(min = 1, max = 254, message = "Длина названия издательства: от 1 до 254 символов")
    private String pubName;
}
