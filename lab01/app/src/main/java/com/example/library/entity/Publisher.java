package com.example.library.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Entity
@Getter @Setter 
public class Publisher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "publisher", cascade = CascadeType.ALL)
    private List<Book> books;

    public Publisher() {} // ✅ Пустой конструктор (нужен JPA)

    public Publisher(String name) { // ✅ Добавленный конструктор
        this.name = name;
    }
}
