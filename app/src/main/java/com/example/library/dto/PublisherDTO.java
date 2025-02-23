package com.example.library.dto;

// Аннотация @NoArgsConstructor - сокращение для создания конструктора без параметров
import lombok.NoArgsConstructor;

// Аннотация @Getter - сокращение для создания геттеров (методов, которые возвращают значения полей)
// для всех полей класса
import lombok.Getter;

// Аннотация @Setter - сокращение для создания сеттеров (методов, которые устанавливают значения полей)
// для всех полей класса
import lombok.Setter;

// Интерфейс Set - это интерфейс, который задает контракт для работы с неупорядоченными коллекциями
// Set<E> - это интерфейс, который задает контракт для работы с неупорядоченными коллекциями,
// содержащими элементы типа E.
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class PublisherDTO {

    private Long id;
    private String name;

    public PublisherDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
