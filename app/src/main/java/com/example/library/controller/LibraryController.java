package com.example.library.controller;

import com.example.library.dto.LibraryDTO;
import com.example.library.service.LibraryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/library")
public class LibraryController {
    private final LibraryService libraryService;

    public LibraryController(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    // 📌 Получаем библиотеку с книгами
    @GetMapping
    public ResponseEntity<LibraryDTO> getLibrary() {
        return ResponseEntity.ok(libraryService.getLibraryWithBooks());
    }

    // 📌 Добавляем книгу в библиотеку
    // @PostMapping("/book")
    // public ResponseEntity<Void> addBookToLibrary(@RequestBody String bookTitle) {
    //     libraryService.addBookToLibrary(bookTitle);
    //     return ResponseEntity.ok().build();
    // }
}
