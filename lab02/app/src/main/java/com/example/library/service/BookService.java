package com.example.library.service;

import com.example.library.dto.BookDTO;
import com.example.library.entity.*;
import com.example.library.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class BookService {

    private final BookDao bookDao;
    private final AuthorDao authorDao;
    private final PublisherDao publisherDao;
    private final CategoryDao categoryDao;
    private final LibraryDao libraryDao;

    public BookService(BookDao bookDao,
                       AuthorDao authorDao,
                       PublisherDao publisherDao,
                       CategoryDao categoryDao,
                       LibraryDao libraryDao) {
        this.bookDao = bookDao;
        this.authorDao = authorDao;
        this.publisherDao = publisherDao;
        this.categoryDao = categoryDao;
        this.libraryDao = libraryDao;
    }

    public List<BookDTO> getAllBooks() {
        return bookDao.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public BookDTO getBookById(Long id) {
        return bookDao.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new RuntimeException("Книга не найдена"));
    }

    public BookDTO createBook(BookDTO bookDTO) {
        Book book = convertToEntity(bookDTO);
        bookDao.save(book);
        addBookToLibrary(book.getTitle());
        return convertToDTO(book);
    }

    public BookDTO updateBook(Long id, BookDTO bookDTO) {
        Book existingBook = bookDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Книга не найдена"));

        existingBook.setTitle(bookDTO.getTitle());
        existingBook.setAuthor(authorDao.findByName(bookDTO.getAuthorName())
                .orElseGet(() -> authorDao.save(new Author(bookDTO.getAuthorName()))));

        existingBook.setPublisher(publisherDao.findByName(bookDTO.getPublisherName())
                .orElseGet(() -> publisherDao.save(new Publisher(bookDTO.getPublisherName()))));

        Set<Category> categories = bookDTO.getCategories().stream()
                .map(name -> categoryDao.findByName(name)
                        .orElseGet(() -> categoryDao.save(new Category(name))))
                .collect(Collectors.toSet());

        existingBook.setTitle(bookDTO.getTitle());
        existingBook.setCategories(categories);

        bookDao.save(existingBook);

        return convertToDTO(existingBook);
    }

    public void deleteBook(Long id) {
        Book book = bookDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Книга не найдена"));
        bookDao.delete(book);
    }

    private BookDTO convertToDTO(Book book) {
        return new BookDTO(
                book.getId(),
                book.getTitle(),
                book.getAuthor().getName(),
                book.getPublisher().getName(),
                book.getCategories().stream().map(Category::getName).collect(Collectors.toSet())
        );
    }

    private Book convertToEntity(BookDTO bookDTO) {
        Author author = authorDao.findByName(bookDTO.getAuthorName())
                .orElseGet(() -> authorDao.save(new Author(bookDTO.getAuthorName())));

        Publisher publisher = publisherDao.findByName(bookDTO.getPublisherName())
                .orElseGet(() -> publisherDao.save(new Publisher(bookDTO.getPublisherName())));

        Set<Category> categories = bookDTO.getCategories().stream()
                .map(name -> categoryDao.findByName(name)
                        .orElseGet(() -> categoryDao.save(new Category(name))))
                .collect(Collectors.toSet());

        return new Book(bookDTO.getTitle(), author, publisher, categories);
    }

    private void addBookToLibrary(String bookTitle) {
        Library library = libraryDao.findById(1L)
                .orElseThrow(() -> new RuntimeException("Библиотека не найдена"));
        library.getBookTitles().add(bookTitle);
        libraryDao.save(library);
    }
}
