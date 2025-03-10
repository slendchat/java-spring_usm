package com.example.library.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Entity
@Getter @Setter
public class Library {
    @Id
    private Long id = 1L;

    private String name = "Main Library";

    @ElementCollection // Список строк (названий книг)
    private List<String> bookTitles;

    public Library() {}

    public Library(String name, List<String> bookTitles) {
        this.name = name;
        this.bookTitles = bookTitles;
    }
}
