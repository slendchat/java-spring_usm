package com.lab03.sport.service;

import com.lab03.sport.dto.CategoryDTO;
import com.lab03.sport.entity.Category;
import com.lab03.sport.repository.CategoryDAO;
import com.lab03.sport.validation.CategoryValidation;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private final CategoryDAO categoryDAO;
    private final CategoryValidation categoryValidation;

    public CategoryService(CategoryDAO categoryDAO,
                           CategoryValidation categoryValidation) {
        this.categoryDAO = categoryDAO;
        this.categoryValidation = categoryValidation;
    }

    /**
     * Создание категории.
     * @param catId   можно передать null, если создаём новую
     * @param catName имя категории
     * @return DTO созданной категории
     */
    public CategoryDTO createCategory(Long catId, String catName) {
        CategoryDTO dto = new CategoryDTO(catId, catName);
        Errors errors = new BeanPropertyBindingResult(dto, "categoryDTO");
        // проверка существования, если ID передан
        if (catId != null) {
            categoryValidation.validate(catId, errors);
        }
        if (errors.hasErrors()) {
            throw new IllegalArgumentException(errors.toString());
        }

        Category entity = new Category();
        entity.setCatName(catName);
        Long newId = categoryDAO.createCategory(entity);
        return new CategoryDTO(newId, catName);
    }

    /**
     * Обновление категории по ID.
     * @param catId   существующий ID
     * @param catName новое имя
     * @return DTO обновлённой категории
     */
    public CategoryDTO updateCategory(Long catId, String catName) {
        CategoryDTO dto = new CategoryDTO(catId, catName);
        Errors errors = new BeanPropertyBindingResult(dto, "categoryDTO");
        categoryValidation.validate(catId, errors);
        if (errors.hasErrors()) {
            throw new IllegalArgumentException(errors.toString());
        }

        Category entity = new Category();
        entity.setCatId(catId);
        entity.setCatName(catName);
        categoryDAO.updateCategory(entity);
        return getCategoryById(catId);
    }

    /**
     * Получение всех категорий.
     */
    public List<CategoryDTO> getAllCategories() {
        return categoryDAO.getAllCategories().stream()
            .map(c -> new CategoryDTO(c.getCatId(), c.getCatName()))
            .collect(Collectors.toList());
    }

    /**
     * Получение категории по ID с проверкой существования.
     */
    public CategoryDTO getCategoryById(Long catId) {
        Errors errors = new BeanPropertyBindingResult(new CategoryDTO(catId, null), "categoryDTO");
        categoryValidation.validate(catId, errors);
        if (errors.hasErrors()) {
            throw new IllegalArgumentException(errors.toString());
        }

        Category c = categoryDAO.getCategoryById(catId)
            .orElseThrow(() -> new IllegalArgumentException("Категория с ID=" + catId + " не найдена"));
        return new CategoryDTO(c.getCatId(), c.getCatName());
    }

    /**
     * Удаление категории по ID с проверкой существования.
     */
    public void deleteCategoryById(Long catId) {
        Errors errors = new BeanPropertyBindingResult(new CategoryDTO(catId, null), "categoryDTO");
        categoryValidation.validate(catId, errors);
        if (errors.hasErrors()) {
            throw new IllegalArgumentException(errors.toString());
        }

        categoryDAO.deleteCategoryById(catId);
    }
}
