package com.example.library.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Set;

@Entity
@Getter @Setter 
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToMany(mappedBy = "categories") // Обратная связь с книгами
    private Set<Book> books;

    public Category() {} // ✅ Пустой конструктор (нужен JPA)

    public Category(String name) { // ✅ Добавленный конструктор
        this.name = name;
    }
}
