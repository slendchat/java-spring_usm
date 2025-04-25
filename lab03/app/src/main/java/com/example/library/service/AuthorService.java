package com.example.library.service;

import com.example.library.dto.AuthorDTO;
import com.example.library.dto.BookDTO;
import com.example.library.entity.Author;
import com.example.library.entity.Book;
import com.example.library.entity.Publisher;
import com.example.library.repository.AuthorDao;
import com.example.library.repository.BookDao;
import com.example.library.repository.CategoryDao;
import com.example.library.repository.PublisherDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class AuthorService {

    private final AuthorDao authorDao;
    private final BookDao bookDao;
    private final PublisherDao publisherDao;
    private final CategoryDao categoryDao;

    public AuthorService(AuthorDao authorDao,
                         BookDao bookDao,
                         PublisherDao publisherDao,
                         CategoryDao categoryDao) {
        this.authorDao = authorDao;
        this.bookDao = bookDao;
        this.publisherDao = publisherDao;
        this.categoryDao = categoryDao;
    }

    /**
     * Создаёт нового автора с указанным именем.
     */
    public AuthorDTO createAuthor(AuthorDTO dto) {
        Author author = new Author();
        author.setName(dto.getName());
        Author saved = authorDao.save(author);
        return new AuthorDTO(saved.getId(), saved.getName(), Collections.emptyList());
    }

    /**
     * Возвращает список всех авторов (без списка книг).
     */
    public List<AuthorDTO> getAllAuthors() {
        return authorDao.findAll().stream()
                .map(a -> new AuthorDTO(a.getId(), a.getName(), Collections.emptyList()))
                .collect(Collectors.toList());
    }

    /**
     * Возвращает список книг данного автора в виде BookDTO.
     */
    public List<BookDTO> getBooksByAuthor(Long authorId) {
        Author author = authorDao.findById(authorId)
                .orElseThrow(() -> new RuntimeException("Author not found: " + authorId));

        return author.getBookIds().stream()
                .map(bookId -> bookDao.findById(bookId)
                        .orElseThrow(() -> new RuntimeException("Book not found: " + bookId)))
                .map(book -> {
                    BookDTO dto = new BookDTO();
                    dto.setId(book.getId());
                    dto.setTitle(book.getTitle());
                    dto.setAuthorId(book.getAuthorId());
                    dto.setAuthorName(author.getName());
                    dto.setPublisherId(book.getPublisherId());
                    Publisher publisher = publisherDao.findById(book.getPublisherId())
                            .orElseThrow(() -> new RuntimeException("Publisher not found: " + book.getPublisherId()));
                    dto.setPublisherName(publisher.getName());

                    Set<Long> categoryIds = new HashSet<>(book.getCategoryIds());
                    dto.setCategoryIds(categoryIds);
                    Set<String> categoryNames = categoryIds.stream()
                            .map(catId -> categoryDao.findById(catId)
                                    .orElseThrow(() -> new RuntimeException("Category not found: " + catId)))
                            .map(c -> c.getName())
                            .collect(Collectors.toSet());
                    dto.setCategoryNames(categoryNames);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public AuthorDTO getAuthorById(Long id) {
        Author author = authorDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Author not found: " + id));

        List<BookDTO> books = author.getBookIds().stream()
                .map(bookId -> bookDao.findById(bookId)
                        .orElseThrow(() -> new RuntimeException("Book not found: " + bookId)))
                .map(book -> {
                    BookDTO b = new BookDTO();
                    b.setId(book.getId());
                    b.setTitle(book.getTitle());
                    b.setAuthorId(book.getAuthorId());
                    b.setAuthorName(author.getName());
                    b.setPublisherId(book.getPublisherId());
                    publisherDao.findById(book.getPublisherId())
                            .ifPresent(pub -> b.setPublisherName(pub.getName()));
                    Set<String> categoryNames = book.getCategoryIds().stream()
                            .map(catId -> categoryDao.findById(catId)
                                    .orElseThrow(() -> new RuntimeException("Category not found: " + catId)))
                            .map(c -> c.getName())
                            .collect(Collectors.toSet());
                    b.setCategoryNames(categoryNames);
                    return b;
                })
                .collect(Collectors.toList());

        return new AuthorDTO(author.getId(), author.getName(), books);
    }
    /** UPDATE **/
    public AuthorDTO updateAuthor(Long id, AuthorDTO dto) {
        Author author = authorDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Author not found: " + id));
        author.setName(dto.getName());
        authorDao.save(author);
        return getAuthorById(id);
    }

    /** DELETE **/
    public void deleteAuthor(Long id) {
        authorDao.deleteById(id);
    }
}
