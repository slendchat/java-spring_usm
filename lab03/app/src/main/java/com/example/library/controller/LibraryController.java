package com.example.library.controller;

import com.example.library.dto.LibraryDTO;
import com.example.library.service.LibraryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/library")
@Validated
public class LibraryController {

    private final LibraryService libraryService;

    public LibraryController(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    /**
     * Получить все библиотеки
     */
    @GetMapping
    public ResponseEntity<List<LibraryDTO>> getAllLibraries() {
        List<LibraryDTO> list = libraryService.getAllLibraries();
        return ResponseEntity.ok(list);
    }

    /**
     * Получить библиотеку по ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<LibraryDTO> getLibraryById(@PathVariable Long id) {
        LibraryDTO dto = libraryService.getLibraryById(id);
        return ResponseEntity.ok(dto);
    }

    /**
     * Создать новую библиотеку
     */
    @PostMapping
    public ResponseEntity<LibraryDTO> createLibrary(@Valid @RequestBody LibraryDTO libraryDTO) {
        LibraryDTO created = libraryService.createLibrary(libraryDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Обновить существующую библиотеку
     */
    @PutMapping("/{id}")
    public ResponseEntity<LibraryDTO> updateLibrary(@PathVariable Long id,
                                                    @Valid @RequestBody LibraryDTO libraryDTO) {
        LibraryDTO updated = libraryService.updateLibrary(id, libraryDTO);
        return ResponseEntity.ok(updated);
    }

    /**
     * Удалить библиотеку по ID
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLibrary(@PathVariable Long id) {
        libraryService.deleteLibrary(id);
        return ResponseEntity.noContent().build();
    }
}