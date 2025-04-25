package com.example.library.service;

import com.example.library.dto.BookDTO;
import com.example.library.dto.CategoryDTO;
import com.example.library.entity.Category;
import com.example.library.repository.CategoryDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class CategoryService {

    private final CategoryDao categoryDao;
    private final BookService bookService;

    public CategoryService(CategoryDao categoryDao, BookService bookService) {
        this.categoryDao = categoryDao;
        this.bookService = bookService;
    }

    /**
     * Получить все категории (без списка книг)
     */
    public List<CategoryDTO> getAllCategories() {
        return categoryDao.findAll().stream()
            .map(cat -> new CategoryDTO(
                cat.getId(),
                cat.getName(),
                new HashSet<>(cat.getBookIds()),
                Collections.emptySet()
            ))
            .collect(Collectors.toList());
    }

    /**
     * Получить категорию по ID с перечислением BookDTO
     */
    public CategoryDTO getCategoryById(Long id) {
        Category cat = categoryDao.findById(id)
            .orElseThrow(() -> new RuntimeException("Category not found: " + id));
        Set<BookDTO> books = cat.getBookIds().stream()
            .map(bookService::getBookById)
            .collect(Collectors.toSet());
        return new CategoryDTO(
            cat.getId(),
            cat.getName(),
            new HashSet<>(cat.getBookIds()),
            books
        );
    }

    /**
     * Создать новую категорию
     */
    public CategoryDTO createCategory(CategoryDTO dto) {
        Set<Long> inBookIds = dto.getBookIds() != null ? dto.getBookIds() : Collections.emptySet();
        for (Long bookId : inBookIds) {
            bookService.getBookById(bookId); // Validate existence
        }
        Category category = new Category();
        category.setName(dto.getName());
        category.setBookIds(List.copyOf(inBookIds));

        Category saved = categoryDao.save(category);
        return new CategoryDTO(
            saved.getId(),
            saved.getName(),
            new HashSet<>(saved.getBookIds()),
            Collections.emptySet()
        );
    }

    /**
     * Удалить категорию по ID
     */
    public void deleteCategory(Long id) {
        categoryDao.deleteById(id);
    }

    /**
     * Update an existing category's name and book associations
     */
    public CategoryDTO updateCategory(Long id, CategoryDTO dto) {
        Category cat = categoryDao.findById(id)
            .orElseThrow(() -> new RuntimeException("Category not found: " + id));
        // Validate books
        Set<Long> bookIds = dto.getBookIds() != null ? dto.getBookIds() : Collections.emptySet();
        for (Long bookId : bookIds) {
            bookService.getBookById(bookId);
        }
        // Update fields
        cat.setName(dto.getName());
        cat.setBookIds(List.copyOf(bookIds));
        Category updated = categoryDao.save(cat);
        return new CategoryDTO(
            updated.getId(),
            updated.getName(),
            Set.copyOf(updated.getBookIds()),
            Collections.emptySet()
        );
    }
}
