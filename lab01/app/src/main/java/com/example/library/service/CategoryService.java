package com.example.library.service;

import com.example.library.dto.BookDTO;
import com.example.library.dto.CategoryDTO;
import com.example.library.entity.Category;
import com.example.library.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    // 📌 Получаем все категории
    public List<CategoryDTO> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(category -> new CategoryDTO(category.getId(), category.getName()))
                .collect(Collectors.toList());
    }

    // 📌 Создаём категорию
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        Category category = new Category(categoryDTO.getName());
        category = categoryRepository.save(category);
        return new CategoryDTO(category.getId(), category.getName());
    }

    // 📌 Получаем все книги по категории
    public List<BookDTO> getBooksByCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
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
