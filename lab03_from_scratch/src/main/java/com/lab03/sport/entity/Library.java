package com.lab03.sport.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import java.util.List;

@Data
@Table("LIBRARY")
public class Library {
    @Id
    private Long libId;

    // все книги этой библиотеки
    @MappedCollection(idColumn = "library_id")
    private List<Book> books;
}
