package com.lab03.sport.controller;

import com.lab03.sport.dto.BookDTO;
import com.lab03.sport.dto.LibraryDTO;
import com.lab03.sport.service.BookService;
import com.lab03.sport.service.LibraryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/libraries")
public class LibraryController {

    private final LibraryService libraryService;
    private final BookService bookService;

    public LibraryController(LibraryService libraryService, BookService bookService) {
        this.libraryService = libraryService;
        this.bookService = bookService;
    }

    /**
     * POST /libraries
     * Создание библиотеки.
     * @param libId    ID библиотеки (может быть null при создании новой)
     * @param libName  название библиотеки
     */
    @PostMapping
    public ResponseEntity<LibraryDTO> createLibrary(
            @RequestParam(required = false) Long libId,
            @RequestParam String libName
    ) {
        LibraryDTO created = libraryService.createLibrary(libId, libName);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    /**
     * PUT /libraries/{id}
     * Обновление библиотеки по ID.
     * @param id       существующий ID библиотеки
     * @param libName  новое название библиотеки
     */
    @PutMapping("/{id}")
    public ResponseEntity<LibraryDTO> updateLibrary(
            @PathVariable("id") Long id,
            @RequestParam String libName
    ) {
        LibraryDTO updated = libraryService.updateLibrary(id, libName);
        return ResponseEntity.ok(updated);
    }

    /**
     * GET /libraries
     * Получение всех библиотек.
     */
    @GetMapping
    public List<LibraryDTO> getAllLibraries() {
        return libraryService.getAllLibraries();
    }

    /**
     * GET /libraries/{id}
     * Получение библиотеки по ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<LibraryDTO> getLibraryById(@PathVariable("id") Long id) {
        LibraryDTO dto = libraryService.getLibraryById(id);
        return ResponseEntity.ok(dto);
    }

    /**
     * DELETE /libraries/{id}
     * Удаление библиотеки по ID и всех книг этой библиотеки.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLibrary(@PathVariable("id") Long id) {
        // сначала удаляем все книги из этой библиотеки
        List<BookDTO> booksInLib = bookService.getAllBooks().stream()
            .filter(b -> b.getLibrary().getLibId().equals(id))
            .collect(Collectors.toList());
        booksInLib.forEach(b -> bookService.deleteBookById(b.getBookId()));

        // затем удаляем саму библиотеку
        libraryService.deleteLibraryById(id);
        return ResponseEntity.noContent().build();
    }
}
