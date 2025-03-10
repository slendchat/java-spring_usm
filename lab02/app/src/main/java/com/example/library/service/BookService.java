package com.example.library.service;

import com.example.library.dto.BookDTO;
import com.example.library.repository.AuthorRepository;
import com.example.library.repository.BookRepository;
import com.example.library.repository.CategoryRepository;
import com.example.library.repository.PublisherRepository;
import com.example.library.repository.LibraryRepository;
import com.example.library.entity.Book;
import com.example.library.entity.Category;
import com.example.library.entity.Author;
import com.example.library.entity.Publisher;
import com.example.library.entity.Library;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookService {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final PublisherRepository publisherRepository;
    private final CategoryRepository categoryRepository;
    private final LibraryRepository libraryRepository;

    public BookService(BookRepository bookRepository, AuthorRepository authorRepository,
                       PublisherRepository publisherRepository, CategoryRepository categoryRepository,
                       LibraryRepository libraryRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.publisherRepository = publisherRepository;
        this.categoryRepository = categoryRepository;
        this.libraryRepository = libraryRepository;
    }

    // 📌 Получаем все книги
    public List<BookDTO> getAllBooks() {
        return bookRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // 📌 Получаем книгу по ID
    public BookDTO getBookById(Long id) {
        return bookRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new RuntimeException("Книга не найдена"));
    }

    // 📌 Создаём книгу
    @Transactional
    public BookDTO createBook(BookDTO bookDTO) {
        addBookToLibrary(bookDTO.getTitle());
        return convertToDTO(bookRepository.save(convertToEntity(bookDTO)));
    }

    // 📌 Обновляем книгу
    @Transactional
    public BookDTO updateBook(Long id, BookDTO bookDTO) {
        return bookRepository.findById(id)
                .map(existingBook -> {
                    existingBook.setTitle(bookDTO.getTitle());
                    return convertToDTO(bookRepository.save(existingBook));
                })
                .orElseThrow(() -> new RuntimeException("Книга не найдена"));
    }

    // 📌 Удаляем книгу
    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new RuntimeException("Книга не найдена");
        }
        bookRepository.deleteById(id);
    }

    // 📌 Преобразование из `Book` в `BookDTO`
    private BookDTO convertToDTO(Book book) {
        return new BookDTO(
                book.getId(),
                book.getTitle(),
                book.getAuthor().getName(),
                book.getPublisher().getName(),
                book.getCategories().stream().map(Category::getName).collect(Collectors.toSet())
        );
    }

    // 📌 Преобразование из `BookDTO` в `Book`
    private Book convertToEntity(BookDTO bookDTO) {
        return new Book(
                bookDTO.getTitle(),
                authorRepository.findByName(bookDTO.getAuthorName())
                        .orElseGet(() -> authorRepository.save(new Author(bookDTO.getAuthorName()))),
                publisherRepository.findByName(bookDTO.getPublisherName())
                        .orElseGet(() -> publisherRepository.save(new Publisher(bookDTO.getPublisherName()))),
                bookDTO.getCategories().stream()
                        .map(categoryName -> categoryRepository.findByName(categoryName)
                                .orElseGet(() -> categoryRepository.save(new Category(categoryName))))
                        .collect(Collectors.toSet())
        );
    }

    private void addBookToLibrary(String bookTitle) {
        Library library = libraryRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Библиотека не найдена"));
        List<String> updatedTitles = library.getBookTitles();
        updatedTitles.add(bookTitle);
        library.setBookTitles(updatedTitles);
        libraryRepository.save(library);
    }
}
