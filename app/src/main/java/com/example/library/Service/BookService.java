package com.example.library.service;

import com.example.library.dto.BookDTO;
import com.example.library.entity.Author;
import com.example.library.entity.Book;
import com.example.library.entity.Category;
import com.example.library.entity.Publisher;
import com.example.library.repository.AuthorRepository;
import com.example.library.repository.BookRepository;
import com.example.library.repository.CategoryRepository;
import com.example.library.repository.PublisherRepository;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BookService {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final PublisherRepository publisherRepository;
    private final CategoryRepository categoryRepository;

    public BookService(BookRepository bookRepository, AuthorRepository authorRepository,
                       PublisherRepository publisherRepository, CategoryRepository categoryRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.publisherRepository = publisherRepository;
        this.categoryRepository = categoryRepository;
    }

    // 📌 Получаем все книги
    public List<BookDTO> getAllBooks() {
        return bookRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // 📌 Получаем книгу по ID
    public BookDTO getBookById(Long id) {
        return bookRepository.findById(id).map(this::convertToDTO)
                .orElseThrow(() -> new RuntimeException("Книга не найдена"));
    }

    // 📌 Создаём книгу
    @Transactional
    public BookDTO createBook(BookDTO bookDTO) {
        Author author = authorRepository.findByName(bookDTO.getAuthorName())
                .orElseGet(() -> {
                    Author newAuthor = new Author();
                    newAuthor.setName(bookDTO.getAuthorName());
                    return authorRepository.save(newAuthor);
                });

        Publisher publisher = publisherRepository.findByName(bookDTO.getPublisherName())
                .orElseGet(() -> {
                    Publisher newPublisher = new Publisher();
                    newPublisher.setName(bookDTO.getPublisherName());
                    return publisherRepository.save(newPublisher);
                });

        Set<Category> categories = bookDTO.getCategories().stream()
                .map(categoryName -> categoryRepository.findByName(categoryName)
                        .orElseGet(() -> {
                            Category newCategory = new Category();
                            newCategory.setName(categoryName);
                            return categoryRepository.save(newCategory);
                        }))
                .collect(Collectors.toSet());

        Book book = new Book();
        book.setTitle(bookDTO.getTitle());
        book.setAuthor(author);
        book.setPublisher(publisher);
        book.setCategories(categories);

        Book savedBook = bookRepository.save(book);
        return convertToDTO(savedBook);
    }

    // 📌 Обновляем книгу
    public BookDTO updateBook(Long id, BookDTO bookDTO) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Книга не найдена"));

        book.setTitle(bookDTO.getTitle());

        Book updatedBook = bookRepository.save(book);
        return convertToDTO(updatedBook);
    }

    // 📌 Удаляем книгу
    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new RuntimeException("Книга не найдена");
        }
        bookRepository.deleteById(id);
    }

    // 📌 Конвертация Entity -> DTO
    private BookDTO convertToDTO(Book book) {
        return new BookDTO(
                book.getId(),
                book.getTitle(),
                book.getAuthor().getName(),
                book.getPublisher().getName(),
                book.getCategories().stream().map(Category::getName).collect(Collectors.toSet())
        );
    }

    // 📌 Пример метода для получения ID из имени (можно заменить на `findByName()`)
    private Long getIdFromName(String name) {
        return Math.abs(name.hashCode() % 1000L); // Имитация ID на основе имени
    }
}
