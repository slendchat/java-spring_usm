package com.lab03.sport.validation;

import com.lab03.sport.dto.BookDTO;
import com.lab03.sport.repository.BookDAO;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class BookValidation implements Validator {

    private final BookDAO bookDAO;
    private final AuthorValidation authorValidation;
    private final PublisherValidation publisherValidation;
    private final CategoryValidation categoryValidation;
    private final LibraryValidation libraryValidation;

    public BookValidation(BookDAO bookDAO,
                          AuthorValidation authorValidation,
                          PublisherValidation publisherValidation,
                          CategoryValidation categoryValidation,
                          LibraryValidation libraryValidation) {
        this.bookDAO             = bookDAO;
        this.authorValidation    = authorValidation;
        this.publisherValidation = publisherValidation;
        this.categoryValidation  = categoryValidation;
        this.libraryValidation   = libraryValidation;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return BookDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        BookDTO dto = (BookDTO) target;

        // 1) Проверка существования книги при update
        Long bookId = dto.getBookId();
        if (bookId != null && bookDAO.getBookById(bookId).isEmpty()) {
            errors.rejectValue("bookId", "NotFound", "Книга с ID=" + bookId + " не найдена");
        }

        // 2) Валидация названия книги
        String name = dto.getBookName();
        if (!StringUtils.hasText(name) || name.length() > 254) {
            errors.rejectValue("bookName", "Size", "Название должно быть от 1 до 254 символов");
        }

        // 3) Валидация связанных сущностей через отдельные валидаторы
        if (dto.getAuthor() != null) {
            authorValidation.validate(dto.getAuthor().getAuthId(), errors);
        }
        if (dto.getPublisher() != null) {
            publisherValidation.validate(dto.getPublisher().getPubId(), errors);
        }
        if (dto.getCategory() != null) {
            categoryValidation.validate(dto.getCategory().getCatId(), errors);
        }
        if (dto.getLibrary() != null) {
            libraryValidation.validate(dto.getLibrary().getLibId(), errors);
        }
    }
    public void validateID(Object target, Errors errors) {
        BookDTO dto = (BookDTO) target;
        // 1) Проверка существования книги
        Long bookId = dto.getBookId();
        if (bookId != null && bookDAO.getBookById(bookId).isEmpty()) {
            errors.rejectValue("bookId", "NotFound", "Книга с ID=" + bookId + " не найдена");
        }
    }
}
