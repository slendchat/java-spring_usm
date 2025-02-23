package com.example.library.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class LibraryDTO {
    private List<String> bookTitles; // 📌 Только названия книг

    public LibraryDTO() {}

    public LibraryDTO(List<String> bookTitles) {
        this.bookTitles = bookTitles;
    }
}
