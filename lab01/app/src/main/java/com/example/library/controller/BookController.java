// Это пакет, в котором находится наш контроллер
package com.example.library.controller;

// Мы импортируем класс BookDTO, который является DTO (Data Transfer Object) для сущности Book
import com.example.library.dto.BookDTO;

// Мы импортируем интерфейс BookService, который содержит методы для работы с книгами
import com.example.library.service.BookService;

// Мы импортируем класс ResponseEntity, который используется для возвращения ответа из контроллера.
// ResponseEntity<T> - это обертка для тела ответа, которая может содержать дополнительную информацию,
// такую как код состояния HTTP-ответа, заголовки и т.д.
import org.springframework.http.ResponseEntity;

// Мы импортируем аннотации для определения путей и методов HTTP.
// Аннотации - это метаинформация, которая добавляется к коду для изменения его поведения.
// Например, аннотация @GetMapping("/books") указывает, что метод должен быть вызван,
// когда приходит GET-запрос на путь "/books".
import org.springframework.web.bind.annotation.*;

// Мы импортируем функциональный интерфейс List, который будет использоваться для получения списка книг.
// List - это интерфейс, который задает контракт для работы с коллекциями.
// List<T> - это интерфейс, который задает контракт для работы с коллекциями,
// содержащими элементы типа T.
import java.util.List;


// GET : retrieves information or data from a specified resource.
// POST : submits data to be processed to a specified resource.
// PUT : updates a specified resource with new data.
// DELETE : deletes a specified resource.
// PATCH : partially updates a specified resource.


// @RestController - это аннотация, которая указывает, что данный класс является контроллером REST API.
// Она импортируется из org.springframework.web.bind.annotation пакета.
// @RestController - это сокращение для @Controller + @ResponseBody.
// @Controller - это аннотация, которая указывает, что данный класс является контроллером,
// а @ResponseBody - это аннотация, которая указывает, что возвращаемое значение метода
// будет записано в тело ответа без какой-либо дополнительной обработки.

// @RequestMapping - это аннотация, которая указывает, какой путь будет использоваться
// для вызова метода, аннотированного этой аннотацией.
// Она импортируется из org.springframework.web.bind.annotation пакета.
// @RequestMapping("/books") - это аннотация, которая указывает, что для вызова методов
// контроллера будет использоваться путь "/books".
// Например, если у нас есть метод
// @GetMapping("/books")
// public List<BookDTO> getBooks() {
//     return bookService.getAllBooks();
// }
// то он будет вызван, когда приходит GET-запрос на путь "/books".

@RestController // этот класс — REST API контроллер
@RequestMapping("/books") // Базовый путь для всех запросов
public class BookController {
    // bookService - это сервис, который занимается бизнес-логикой книг.
    // Мы создаем переменную, чтобы иметь доступ к этому сервису в любом месте контроллера.
    // Метод getBookService() позволяет инжектировать (.inject ) сервис, когда создается экземпляр контроллера.
    private final BookService bookService;

    // это конструктор класса BookController, он будет вызван при создании объекта BookController
    // Spring передаст объект BookService, который будет использоваться в контроллере
    public BookController(BookService bookService) {
        // здесь мы присваиваем значение сервиса, полученному от Spring, к нашей переменной
        this.bookService = bookService;
    }

    // 📌 Получить все книги GET
    @GetMapping
    public List<BookDTO> getBooks() {
        // 📌 Обращаемся к методу getAllBooks() в bookService
        // Этот метод получает все книги из bookRepository и преобразует их в DTO
        // Сначала он извлекает все записи из bookRepository в виде объектов Book
        // Затем он использует Stream API для преобразования каждого объекта Book в BookDTO
        // В результате возвращается список объектов BookDTO
        return bookService.getAllBooks();
    }

    // 📌 Получить книгу по ID GET
    @GetMapping("/{id}")
    public BookDTO getBookById(@PathVariable Long id) {
        return bookService.getBookById(id);
    }

    // ResponseEntity - это обертка для тела ответа, которая может содержать дополнительную информацию,
    // такую как код состояния HTTP-ответа, заголовки и т.д.
    // Мы используем ResponseEntity здесь, потому что возвращаемый тип метода getBookById() - BookDTO,
    // а не ResponseEntity<BookDTO>. Spring автоматически преобразует BookDTO в тело ответа.
    //

    // 📌 Создать книгу POST
    // @PostMapping
    // public ResponseEntity<BookDTO> createBook(@RequestBody BookDTO bookDTO) {
    //     return ResponseEntity.ok(bookService.createBook(bookDTO));
    // }
    @PostMapping
    public BookDTO createBook(@RequestBody BookDTO bookDTO) {
        return bookService.createBook(bookDTO);
    }

    // 📌 Обновить книгу по ID PUT
    // @PutMapping("/{id}")
    // public ResponseEntity<BookDTO> updateBook(@PathVariable Long id, @RequestBody BookDTO bookDTO) {
    //     return ResponseEntity.ok(bookService.updateBook(id, bookDTO));
    // }
    @PutMapping("/{id}")
    public BookDTO updateBook(@PathVariable Long id, @RequestBody BookDTO bookDTO) {
        return bookService.updateBook(id, bookDTO);
    }
    // 📌 Удалить книгу DELETE
    // @DeleteMapping("/{id}")
    // public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
    //     bookService.deleteBook(id);
    //     return ResponseEntity.noContent().build();
    // }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.ok("Book deleted successfully");
    }
}

