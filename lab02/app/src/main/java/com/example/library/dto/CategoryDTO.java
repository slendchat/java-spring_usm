package com.example.library.dto;

// Аннотация @NoArgsConstructor - сокращение для создания конструктора без параметров
import lombok.NoArgsConstructor;

// Аннотация @Getter - сокращение для создания геттеров (методов, которые возвращают значения полей)
// для всех полей класса
import lombok.Getter;

// Аннотация @Setter - сокращение для создания сеттеров (методов, которые устанавливают значения полей)
// для всех полей класса
import lombok.Setter;



@Getter
@Setter
@NoArgsConstructor
public class CategoryDTO {

    private Long id;
    private String name;

    public CategoryDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
