// src/main/java/com/lab03/sport/validation/CategoryValidation.java
package com.lab03.sport.validation;

import com.lab03.sport.repository.CategoryDAO;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class CategoryValidation {

    private final CategoryDAO categoryDAO;

    public CategoryValidation(CategoryDAO categoryDAO) {
        this.categoryDAO = categoryDAO;
    }

    /**
     * Проверяет, что категория с таким ID существует.
     */
    public void validate(Long categoryId, Errors errors) {
        if (categoryId == null || categoryDAO.getCategoryById(categoryId).isEmpty()) {
            errors.rejectValue("category.catId",
                    "NotFound",
                    "Категория с ID=" + categoryId + " не найдена");
        }
    }
}
