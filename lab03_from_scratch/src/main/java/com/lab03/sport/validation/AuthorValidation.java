// src/main/java/com/lab03/sport/validation/AuthorValidation.java
package com.lab03.sport.validation;

import com.lab03.sport.repository.AuthorDAO;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

/**
 * Этот класс отвечает за валидацию авторов.
 * Он проверяет, существует ли автор с заданным ID в базе данных.
 * Если автор не найден, то добавляет ошибку в объект ошибок.
 */
@Component
public class AuthorValidation {

    private final AuthorDAO authorDAO;

    public AuthorValidation(AuthorDAO authorDAO) {
        this.authorDAO = authorDAO;
    }

    /**
     * Проверяет, что автор с таким ID существует.
     * @param authorId  ID автора из DTO
     * @param errors    объект для регистрации ошибок валидации
     */
    public void validate(Long authorId, Errors errors) {
        if (authorId == null || authorDAO.getAuthorById(authorId).isEmpty()) {
            errors.rejectValue("author.authId",
                    "NotFound",
                    "Автор с ID=" + authorId + " не найден");
        }
    }
}