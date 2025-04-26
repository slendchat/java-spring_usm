// src/main/java/com/lab03/sport/service/LibraryService.java
package com.lab03.sport.service;

import com.lab03.sport.dto.LibraryDTO;
import com.lab03.sport.entity.Library;
import com.lab03.sport.repository.LibraryDAO;
import com.lab03.sport.validation.LibraryValidation;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LibraryService {

    private final LibraryDAO libraryDAO;
    private final LibraryValidation libraryValidation;

    public LibraryService(LibraryDAO libraryDAO,
                          LibraryValidation libraryValidation) {
        this.libraryDAO = libraryDAO;
        this.libraryValidation = libraryValidation;
    }

    /**
     * Создание библиотеки.
     */
    public LibraryDTO createLibrary(Long libId, String libName) {
        LibraryDTO dto = new LibraryDTO(libId, libName, Collections.emptyList());
        Errors errors = new BeanPropertyBindingResult(dto, "libraryDTO");
        if (libId != null) {
            libraryValidation.validate(libId, errors);
        }
        if (errors.hasErrors()) {
            throw new IllegalArgumentException(errors.toString());
        }
        Library entity = new Library();
        entity.setLibName(libName);
        Long newId = libraryDAO.createLibrary(entity);
        return new LibraryDTO(newId, libName, Collections.emptyList());
    }

    /**
     * Обновление библиотеки по ID.
     */
    public LibraryDTO updateLibrary(Long libId, String libName) {
        LibraryDTO dto = new LibraryDTO(libId, libName, Collections.emptyList());
        Errors errors = new BeanPropertyBindingResult(dto, "libraryDTO");
        libraryValidation.validate(libId, errors);
        if (errors.hasErrors()) {
            throw new IllegalArgumentException(errors.toString());
        }
        Library entity = new Library();
        entity.setLibId(libId);
        entity.setLibName(libName);
        libraryDAO.updateLibrary(entity);
        return getLibraryById(libId);
    }

    /**
     * Получение всех библиотек.
     */
    public List<LibraryDTO> getAllLibraries() {
        return libraryDAO.getAllLibraries().stream()
                .map(lib -> new LibraryDTO(lib.getLibId(), lib.getLibName(), Collections.emptyList()))
                .collect(Collectors.toList());
    }

    /**
     * Получение библиотеки по ID.
     */
    public LibraryDTO getLibraryById(Long libId) {
        Errors errors = new BeanPropertyBindingResult(new LibraryDTO(libId, null, null), "libraryDTO");
        libraryValidation.validate(libId, errors);
        if (errors.hasErrors()) {
            throw new IllegalArgumentException(errors.toString());
        }
        Library lib = libraryDAO.getLibraryById(libId)
                .orElseThrow(() -> new IllegalArgumentException("Библиотека с ID=" + libId + " не найдена"));
        return new LibraryDTO(lib.getLibId(), lib.getLibName(), Collections.emptyList());
    }

    /**
     * Удаление библиотеки по ID.
     */
    public void deleteLibraryById(Long libId) {
        Errors errors = new BeanPropertyBindingResult(new LibraryDTO(libId, null, null), "libraryDTO");
        libraryValidation.validate(libId, errors);
        if (errors.hasErrors()) {
            throw new IllegalArgumentException(errors.toString());
        }
        libraryDAO.deleteLibraryById(libId);
    }
}
