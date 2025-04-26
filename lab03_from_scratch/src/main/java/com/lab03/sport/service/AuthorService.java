package com.lab03.sport.service;

import com.lab03.sport.dto.AuthorDTO;
import com.lab03.sport.entity.Author;
import com.lab03.sport.repository.AuthorDAO;
import com.lab03.sport.validation.AuthorValidation;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthorService {

    private final AuthorDAO authorDAO;
    private final AuthorValidation authorValidation;

    public AuthorService(AuthorDAO authorDAO, AuthorValidation authorValidation) {
        this.authorDAO = authorDAO;
        this.authorValidation = authorValidation;
    }

    /**
     * Создание автора.
     * @param authId   при создании можно передать null
     * @param authName имя автора
     * @return DTO созданного автора с присвоенным ID
     */
    public AuthorDTO createAuthor(Long authId, String authName) {
        AuthorDTO dto = new AuthorDTO(authId, authName);
        Errors errors = new BeanPropertyBindingResult(dto, "authorDTO");
        // если передан authId, проверим его существование (опционально)
        if (authId != null) {
            authorValidation.validate(authId, errors);
        }
        if (errors.hasErrors()) {
            throw new IllegalArgumentException(errors.toString());
        }
        Author author = new Author();
        author.setAuthName(authName);
        Long newId = authorDAO.createAuthor(author);
        return new AuthorDTO(newId, authName);
    }

    /**
     * Обновление автора по ID.
     * @param authId   существующий ID автора
     * @param authName новое имя
     * @return DTO обновлённого автора
     */
    public AuthorDTO updateAuthor(Long authId, String authName) {
        AuthorDTO dto = new AuthorDTO(authId, authName);
        Errors errors = new BeanPropertyBindingResult(dto, "authorDTO");
        authorValidation.validate(authId, errors);
        if (errors.hasErrors()) {
            throw new IllegalArgumentException(errors.toString());
        }
        Author author = new Author();
        author.setAuthId(authId);
        author.setAuthName(authName);
        authorDAO.updateAuthor(author);
        return getAuthorById(authId);
    }

    /**
     * Получение всех авторов.
     */
    public List<AuthorDTO> getAllAuthors() {
        return authorDAO.getAllAuthors().stream()
                .map(a -> new AuthorDTO(a.getAuthId(), a.getAuthName()))
                .collect(Collectors.toList());
    }

    /**
     * Получение автора по ID с проверкой существования.
     */
    public AuthorDTO getAuthorById(Long authId) {
        Errors errors = new BeanPropertyBindingResult(new AuthorDTO(authId, null), "authorDTO");
        authorValidation.validate(authId, errors);
        if (errors.hasErrors()) {
            throw new IllegalArgumentException(errors.toString());
        }
        Author a = authorDAO.getAuthorById(authId)
                .orElseThrow(() -> new IllegalArgumentException("Автор с ID=" + authId + " не найден"));
        return new AuthorDTO(a.getAuthId(), a.getAuthName());
    }

    /**
     * Удаление автора по ID с проверкой существования.
     */
    public void deleteAuthorById(Long authId) {
        Errors errors = new BeanPropertyBindingResult(new AuthorDTO(authId, null), "authorDTO");
        authorValidation.validate(authId, errors);
        if (errors.hasErrors()) {
            throw new IllegalArgumentException(errors.toString());
        }
        authorDAO.deleteAuthorById(authId);
    }
}
