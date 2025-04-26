package com.lab03.sport.controller;

import com.lab03.sport.dto.AuthorDTO;
import com.lab03.sport.service.AuthorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/authors")
public class AuthorController {

    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    /**
     * POST /authors
     * Создание автора.
     * @param authId   ID автора (может быть null при создании новой записи)
     * @param authName имя автора
     */
    @PostMapping
    public ResponseEntity<AuthorDTO> createAuthor(
            @RequestParam(required = false) Long authId,
            @RequestParam String authName
    ) {
        AuthorDTO created = authorService.createAuthor(authId, authName);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    /**
     * PUT /authors/{id}
     * Обновление автора по ID.
     * @param id       существующий ID автора
     * @param authName новое имя автора
     */
    @PutMapping("/{id}")
    public ResponseEntity<AuthorDTO> updateAuthor(
            @PathVariable("id") Long id,
            @RequestParam String authName
    ) {
        AuthorDTO updated = authorService.updateAuthor(id, authName);
        return ResponseEntity.ok(updated);
    }

    /**
     * GET /authors
     * Получение всех авторов.
     */
    @GetMapping
    public List<AuthorDTO> getAllAuthors() {
        return authorService.getAllAuthors();
    }

    /**
     * GET /authors/{id}
     * Получение автора по ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AuthorDTO> getAuthorById(@PathVariable("id") Long id) {
        AuthorDTO dto = authorService.getAuthorById(id);
        return ResponseEntity.ok(dto);
    }

    /**
     * DELETE /authors/{id}
     * Удаление автора по ID.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuthorById(@PathVariable("id") Long id) {
        authorService.deleteAuthorById(id);
        return ResponseEntity.noContent().build();
    }
}
