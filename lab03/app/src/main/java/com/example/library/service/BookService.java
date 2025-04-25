package com.example.library.service;

import com.example.library.dto.BookDTO;
import com.example.library.entity.Book;
import com.example.library.entity.Author;
import com.example.library.entity.Category;
import com.example.library.entity.Library;
import com.example.library.entity.Publisher;
import com.example.library.repository.AuthorDao;
import com.example.library.repository.BookDao;
import com.example.library.repository.CategoryDao;
import com.example.library.repository.LibraryDao;
import com.example.library.repository.PublisherDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
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
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public BookDTO getBookById(Long id) {
        Book book = bookDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found: " + id));
        return toDto(book);
    }

    public BookDTO createBook(BookDTO dto) {
        // Определяем или создаём автора
        Long authorId;
        if (dto.getAuthorId() != null && authorDao.findById(dto.getAuthorId()).isPresent()) {
            authorId = dto.getAuthorId();
        } else {
            authorId = authorDao.save(new Author()).getId();
        }

        // Определяем или создаём издателя
        Long publisherId;
        if (dto.getPublisherId() != null && publisherDao.findById(dto.getPublisherId()).isPresent()) {
            publisherId = dto.getPublisherId();
        } else {
            publisherId = publisherDao.save(new Publisher()).getId();
        }

        // Собираем идентификаторы категорий, создаём новые по names при необходимости
        List<Long> finalCatIds = new ArrayList<>();
        if (dto.getCategoryIds() != null) {
            for (Long catId : dto.getCategoryIds()) {
                if (categoryDao.existsById(catId)) {
                    finalCatIds.add(catId);
                }
            }
        }
        if (dto.getCategoryNames() != null) {
            for (String name : dto.getCategoryNames()) {
                Long id = categoryDao.findByName(name)
                        .orElseGet(() -> categoryDao.save(new Category(null, name, new ArrayList<>())))
                        .getId();
                if (!finalCatIds.contains(id)) finalCatIds.add(id);
            }
        }

        // Создаём книгу
        Book book = new Book(
                null,
                dto.getTitle(),
                authorId,
                publisherId,
                finalCatIds
        );
        Book saved = bookDao.save(book);

        // Обновляем библиотеку
        Library lib = libraryDao.findById(1L)
                .orElseThrow(() -> new RuntimeException("Library not found"));
        lib.getBookTitles().add(saved.getTitle());
        libraryDao.save(lib);

        return toDto(saved);
    }

    public BookDTO updateBook(Long id, BookDTO dto) {
        Book book = bookDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found: " + id));

        // Проверяем связанные сущности
        authorDao.findById(dto.getAuthorId())
                .orElseThrow(() -> new RuntimeException("Author not found: " + dto.getAuthorId()));
        publisherDao.findById(dto.getPublisherId())
                .orElseThrow(() -> new RuntimeException("Publisher not found: " + dto.getPublisherId()));
        for (Long catId : dto.getCategoryIds()) {
            if (!categoryDao.existsById(catId)) {
                throw new RuntimeException("Category not found: " + catId);
            }
        }

        book.setTitle(dto.getTitle());
        book.setAuthorId(dto.getAuthorId());
        book.setPublisherId(dto.getPublisherId());
        book.setCategoryIds(new ArrayList<>(dto.getCategoryIds()));
        Book updated = bookDao.save(book);
        return toDto(updated);
    }

    public void deleteBook(Long id) {
        Book book = bookDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found: " + id));
        // Убираем название книги из библиотеки
        Library lib = libraryDao.findById(1L)
                .orElseThrow(() -> new RuntimeException("Library not found"));
        lib.getBookTitles().remove(book.getTitle());
        libraryDao.save(lib);

        bookDao.deleteById(id);
    }

    private BookDTO toDto(Book book) {
        BookDTO dto = new BookDTO();
        dto.setId(book.getId());
        dto.setTitle(book.getTitle());
        dto.setAuthorId(book.getAuthorId());
        // Имя автора
        dto.setAuthorName(
                authorDao.findById(book.getAuthorId())
                        .map(a -> a.getName())
                        .orElse(null)
        );
        dto.setPublisherId(book.getPublisherId());
        // Имя издателя
        dto.setPublisherName(
                publisherDao.findById(book.getPublisherId())
                        .map(Publisher::getName)
                        .orElse(null)
        );
        Set<Long> catIds = new HashSet<>(book.getCategoryIds());
        dto.setCategoryIds(catIds);
        // Имена категорий
        Set<String> catNames = catIds.stream()
                .map(catId -> categoryDao.findById(catId)
                        .map(c -> c.getName())
                        .orElse(null))
                .filter(name -> name != null)
                .collect(Collectors.toSet());
        dto.setCategoryNames(catNames);
        return dto;
    }
}
