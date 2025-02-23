package com.example.library.service;

import com.example.library.dto.AuthorDTO;
import com.example.library.dto.BookDTO;
import com.example.library.repository.AuthorRepository;
import org.springframework.stereotype.Service;
import com.example.library.entity.Author;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthorService {
    private final AuthorRepository authorRepository;

    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public List<AuthorDTO> getAllAuthors() {
        return authorRepository.findAll().stream()
                .map(author -> new AuthorDTO(author.getId(), author.getName()))
                .collect(Collectors.toList());
    }

    public AuthorDTO createAuthor(AuthorDTO authorDTO) {
        Author author = new Author();
        author.setName(authorDTO.getName());
        return new AuthorDTO(authorRepository.save(author).getId(), author.getName());
    }

     /**
      * Метод возвращает список книг у которых authorId совпадает
      * с id автора, переданным в параметре
      * @param authorId - id автора
      * @return - список книг
      */
     public List<BookDTO> getBooksByAuthor(Long authorId) {
        Author author = authorRepository.findById(authorId)
                .orElseThrow(() -> new RuntimeException("Автор не найден"));

        return author.getBooks().stream()
                .map(book -> new BookDTO(
                        book.getId(),
                        book.getTitle(),
                        book.getAuthor().getName(),
                        book.getPublisher().getName(),
                        book.getCategories().stream().map(c -> c.getName()).collect(Collectors.toSet())
                ))
                .collect(Collectors.toList());
    }
}
