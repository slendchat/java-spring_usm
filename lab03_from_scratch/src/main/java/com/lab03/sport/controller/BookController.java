package com.lab03.sport.controller;

import com.lab03.sport.dto.BookDTO;
import com.lab03.sport.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    /**
     * POST /books
     * Создание книги.
     * @param authorId     ID автора
     * @param publisherId  ID издателя
     * @param categoryId   ID категории
     * @param libraryId    ID библиотеки
     * @param bookName     название книги
     */
    @PostMapping
    public ResponseEntity<BookDTO> createBook(
            @RequestParam Long authorId,
            @RequestParam Long publisherId,
            @RequestParam Long categoryId,
            @RequestParam Long libraryId,
            @RequestParam String bookName
    ) {
        BookDTO created = bookService.createBook(bookName, authorId, publisherId, categoryId, libraryId);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    /**
     * PUT /books/{id}
     * Обновление книги по ID.
     * @param id           ID книги
     * @param authorId     новый ID автора
     * @param publisherId  новый ID издателя
     * @param categoryId   новый ID категории
     * @param libraryId    новый ID библиотеки
     * @param bookName     новое название книги
     */
    @PutMapping("/{id}")
    public ResponseEntity<BookDTO> updateBook(
            @PathVariable("id") Long id,
            @RequestParam Long authorId,
            @RequestParam Long publisherId,
            @RequestParam Long categoryId,
            @RequestParam Long libraryId,
            @RequestParam String bookName
    ) {
        BookDTO updated = bookService.updateBook(id, bookName, authorId, publisherId, categoryId, libraryId);
        return ResponseEntity.ok(updated);
    }

    /**
     * GET /books
     * Получение всех книг.
     */
    @GetMapping
    public List<BookDTO> getAllBooks() {
        return bookService.getAllBooks();
    }

    /**
     * GET /books/{id}
     * Получение книги по ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<BookDTO> getBookById(@PathVariable("id") Long id) {
        BookDTO dto = bookService.getBookById(id);
        return ResponseEntity.ok(dto);
    }

    /**
     * DELETE /books/{id}
     * Удаление книги по ID.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBookById(@PathVariable("id") Long id) {
        bookService.deleteBookById(id);
        return ResponseEntity.noContent().build();
    }
}
