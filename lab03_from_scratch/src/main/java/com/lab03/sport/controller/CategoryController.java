package com.lab03.sport.controller;

import com.lab03.sport.dto.CategoryDTO;
import com.lab03.sport.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /**
     * POST /categories
     * Создание категории.
     * @param catId   ID категории (может быть null при создании новой)
     * @param catName название категории
     */
    @PostMapping
    public ResponseEntity<CategoryDTO> createCategory(
            @RequestParam(required = false) Long catId,
            @RequestParam String catName
    ) {
        CategoryDTO created = categoryService.createCategory(catId, catName);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    /**
     * PUT /categories/{id}
     * Обновление категории по ID.
     * @param id      существующий ID категории
     * @param catName новое название категории
     */
    @PutMapping("/{id}")
    public ResponseEntity<CategoryDTO> updateCategory(
            @PathVariable("id") Long id,
            @RequestParam String catName
    ) {
        CategoryDTO updated = categoryService.updateCategory(id, catName);
        return ResponseEntity.ok(updated);
    }

    /**
     * GET /categories
     * Получение всех категорий.
     */
    @GetMapping
    public List<CategoryDTO> getAllCategories() {
        return categoryService.getAllCategories();
    }

    /**
     * GET /categories/{id}
     * Получение категории по ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable("id") Long id) {
        CategoryDTO dto = categoryService.getCategoryById(id);
        return ResponseEntity.ok(dto);
    }

    /**
     * DELETE /categories/{id}
     * Удаление категории по ID.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategoryById(@PathVariable("id") Long id) {
        categoryService.deleteCategoryById(id);
        return ResponseEntity.noContent().build();
    }
}
