package com.example.library.service;

import com.example.library.dto.AuthorDTO;
import com.example.library.dto.BookDTO;
import com.example.library.entity.Author;
import com.example.library.entity.Category;
import com.example.library.repository.AuthorDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class AuthorService {

    private final AuthorDao authorDao;

    public AuthorService(AuthorDao authorDao) {
        this.authorDao = authorDao;
    }
    public AuthorDTO createAuthor(AuthorDTO authorDTO) {
        Author author = new Author(authorDTO.getName());
        authorDao.save(author);
        return new AuthorDTO(author.getId(), author.getName());
    }
    
    public List<AuthorDTO> getAllAuthors() {
        return authorDao.findAll().stream()
                .map(author -> new AuthorDTO(author.getId(), author.getName()))
                .collect(Collectors.toList());
    }

    public List<BookDTO> getBooksByAuthor(Long authorId) {
        Author author = authorDao.findById(authorId)
                .orElseThrow(() -> new RuntimeException("Автор не найден"));

        return author.getBooks().stream()
                .map(book -> new BookDTO(
                        book.getId(),
                        book.getTitle(),
                        author.getName(),
                        book.getPublisher().getName(),
                        book.getCategories().stream().map(Category::getName).collect(Collectors.toSet())
                ))
                .collect(Collectors.toList());
    }
}