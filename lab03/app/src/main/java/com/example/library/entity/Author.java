package com.example.library.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class Author {

    private Long id;
    private String name;
    private List<Long> bookIds;
    public Author(String name) {
        this.name = name;
    }
}
