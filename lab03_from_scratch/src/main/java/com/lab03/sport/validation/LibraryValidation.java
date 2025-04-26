// src/main/java/com/lab03/sport/validation/LibraryValidation.java
package com.lab03.sport.validation;

import com.lab03.sport.repository.LibraryDAO;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class LibraryValidation {

    private final LibraryDAO libraryDAO;

    public LibraryValidation(LibraryDAO libraryDAO) {
        this.libraryDAO = libraryDAO;
    }

    /**
     * Проверяет, что библиотека с таким ID существует.
     */
    public void validate(Long libraryId, Errors errors) {
        if (libraryId == null || libraryDAO.getLibraryById(libraryId).isEmpty()) {
            errors.rejectValue("libId",
                    "NotFound",
                    "Библиотека с ID=" + libraryId + " не найдена");
        }
    }
}
