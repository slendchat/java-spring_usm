package com.example.library.service;

import com.example.library.dto.BookDTO;
import com.example.library.dto.CategoryDTO;
import com.example.library.entity.Category;
import com.example.library.repository.CategoryDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CategoryService {

    private final CategoryDao categoryDao;

    public CategoryService(CategoryDao categoryDao) {
        this.categoryDao = categoryDao;
    }

    public List<CategoryDTO> getAllCategories() {
        return categoryDao.findAll().stream()
                .map(category -> new CategoryDTO(category.getId(), category.getName()))
                .collect(Collectors.toList());
    }

    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        Category category = new Category(categoryDTO.getName());
        categoryDao.save(category);
        return new CategoryDTO(category.getId(), category.getName());
    }

    public List<BookDTO> getBooksByCategory(Long categoryId) {
        Category category = categoryDao.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Категория не найдена"));

        return category.getBooks().stream()
                .map(book -> new BookDTO(
                        book.getId(),
                        book.getTitle(),
                        book.getAuthor().getName(),
                        book.getPublisher().getName(),
                        book.getCategories().stream().map(Category::getName).collect(Collectors.toSet())
                ))
                .collect(Collectors.toList());
    }
}
