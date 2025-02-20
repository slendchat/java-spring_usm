package com.example.library.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Entity // Делаем класс сущностью для базы данных
@Getter @Setter @NoArgsConstructor // Автоматически создаёт геттеры, сеттеры и конструктор без аргументов
public class Author {
    @Id // Это PRIMARY KEY
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Автоинкремент ID
    private Long id;

    private String name;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL) // Связь "один ко многим"
    private List<Book> books; // Один автор может написать много книг
}
