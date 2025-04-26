// src/main/java/com/lab03/sport/entity/Library.java
package com.lab03.sport.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("LIBRARY")
public class Library {
    @Id
    private Long libId;
    private String libName;

    @MappedCollection(idColumn = "library_id")
    private List<Book> books;
}
