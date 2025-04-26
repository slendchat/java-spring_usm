package com.lab03.sport.service;

import com.lab03.sport.dto.*;
import com.lab03.sport.entity.Book;
import com.lab03.sport.entity.Library;
import com.lab03.sport.repository.*;
import com.lab03.sport.validation.BookValidation;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookService {

    private final BookDAO bookDAO;
    private final AuthorDAO authorDAO;
    private final PublisherDAO publisherDAO;
    private final CategoryDAO categoryDAO;
    private final LibraryDAO libraryDAO;
    private final BookValidation bookValidation;

    public BookService(BookDAO bookDAO,
                       AuthorDAO authorDAO,
                       PublisherDAO publisherDAO,
                       CategoryDAO categoryDAO,
                       LibraryDAO libraryDAO,
                       BookValidation bookValidation) {
        this.bookDAO = bookDAO;
        this.authorDAO = authorDAO;
        this.publisherDAO = publisherDAO;
        this.categoryDAO = categoryDAO;
        this.libraryDAO = libraryDAO;
        this.bookValidation = bookValidation;
    }

    /**
     * POST — создание книги.
     */
    public BookDTO createBook(String bookName,
                              Long authorId,
                              Long publisherId,
                              Long categoryId,
                              Long libraryId) {
        // DTO для валидации
        BookDTO dto = new BookDTO();
        dto.setBookName(bookName);
        dto.setAuthor(new AuthorDTO(authorId, null));
        dto.setPublisher(new PublisherDTO(publisherId, null));
        dto.setCategory(new CategoryDTO(categoryId, null));
        LibraryDTO libDto = new LibraryDTO(); 
        libDto.setLibId(libraryId);
        dto.setLibrary(libDto);

        Errors errors = new BeanPropertyBindingResult(dto, "bookDTO");
        bookValidation.validate(dto, errors);
        if (errors.hasErrors()) {
            throw new IllegalArgumentException(errors.toString());
        }

        // Сохранение
        Book book = new Book();
        book.setBookName(bookName);
        book.setAuthorId(authorId);
        book.setPublisherId(publisherId);
        book.setCategoryId(categoryId);
        book.setLibraryId(libraryId);
        Long newId = bookDAO.createBook(book);

        return getBookById(newId);
    }

    /**
     * PUT — обновление книги по ID.
     */
    public BookDTO updateBook(Long bookId,
                              String bookName,
                              Long authorId,
                              Long publisherId,
                              Long categoryId,
                              Long libraryId) {
        // DTO для валидации
        BookDTO dto = new BookDTO();
        dto.setBookId(bookId);
        dto.setBookName(bookName);
        dto.setAuthor(new AuthorDTO(authorId, null));
        dto.setPublisher(new PublisherDTO(publisherId, null));
        dto.setCategory(new CategoryDTO(categoryId, null));
        LibraryDTO libDto = new LibraryDTO();
        libDto.setLibId(libraryId);
        dto.setLibrary(libDto);

        Errors errors = new BeanPropertyBindingResult(dto, "bookDTO");
        bookValidation.validate(dto, errors);
        if (errors.hasErrors()) {
            throw new IllegalArgumentException(errors.toString());
        }

        // Обновление
        Book book = new Book();
        book.setBookId(bookId);
        book.setBookName(bookName);
        book.setAuthorId(authorId);
        book.setPublisherId(publisherId);
        book.setCategoryId(categoryId);
        book.setLibraryId(libraryId);
        bookDAO.updateBook(book);

        return getBookById(bookId);
    }

    /**
     * GET — получить все книги с вложенными DTO.
     */
    public List<BookDTO> getAllBooks() {
        return bookDAO.getAllBooks().stream()
                      .map(this::mapEntityToDto)
                      .collect(Collectors.toList());
    }

    /**
     * GET — получить книгу по ID с проверкой существования.
     */
    public BookDTO getBookById(Long bookId) {
        BookDTO checkDto = new BookDTO();
        checkDto.setBookId(bookId);
        Errors errors = new BeanPropertyBindingResult(checkDto, "bookDTO");
        bookValidation.validateID(checkDto, errors);
        if (errors.hasErrors()) {
            throw new IllegalArgumentException(errors.toString());
        }

        Book book = bookDAO.getBookById(bookId)
                           .orElseThrow(() -> new IllegalArgumentException("Книга не найдена"));
        return mapEntityToDto(book);
    }

    /**
     * DELETE — удалить книгу по ID с проверкой существования.
     */
    public void deleteBookById(Long bookId) {
        BookDTO checkDto = new BookDTO();
        checkDto.setBookId(bookId);
        Errors errors = new BeanPropertyBindingResult(checkDto, "bookDTO");
        bookValidation.validateID(checkDto, errors);
        if (errors.hasErrors()) {
            throw new IllegalArgumentException(errors.toString());
        }

        bookDAO.deleteBookById(bookId);
    }

    // Преобразование entity → DTO
    private BookDTO mapEntityToDto(Book book) {
        // Автор
        var auth = authorDAO.getAuthorById(book.getAuthorId())
                            .orElseThrow(() -> new IllegalArgumentException("Автор не найден"));
        AuthorDTO authDto = new AuthorDTO(auth.getAuthId(), auth.getAuthName());

        // Издатель
        var pub = publisherDAO.getPublisherById(book.getPublisherId())
                              .orElseThrow(() -> new IllegalArgumentException("Издатель не найден"));
        PublisherDTO pubDto = new PublisherDTO(pub.getPubId(), pub.getPubName());

        // Категория
        var cat = categoryDAO.getCategoryById(book.getCategoryId())
                             .orElseThrow(() -> new IllegalArgumentException("Категория не найдена"));
        CategoryDTO catDto = new CategoryDTO(cat.getCatId(), cat.getCatName());

        // Библиотека
        Library libEntity = libraryDAO.getLibraryById(book.getLibraryId())
                                      .orElseThrow(() -> new IllegalArgumentException("Библиотека не найдена"));
        LibraryDTO libDto = new LibraryDTO(
            libEntity.getLibId(),
            libEntity.getLibName(),
            Collections.emptyList()
        );

        return new BookDTO(
            book.getBookId(),
            book.getBookName(),
            authDto,
            pubDto,
            catDto,
            libDto
        );
    }
}
