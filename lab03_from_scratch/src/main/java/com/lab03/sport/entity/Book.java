package com.lab03.sport.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("BOOK")
public class Book {
    @Id
    private Long bookId;
    private String bookName;
    private Long authorId;
    private Long publisherId;
    private Long categoryId;
    // поле libraryId не нужно — JDBC сам подставит FK из @MappedCollection
}
