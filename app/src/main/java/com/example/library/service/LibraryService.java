package com.example.library.service;

import com.example.library.dto.LibraryDTO;
import com.example.library.entity.Library;
import com.example.library.repository.LibraryRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LibraryService {
    private final LibraryRepository libraryRepository;

    public LibraryService(LibraryRepository libraryRepository) {
        this.libraryRepository = libraryRepository;
    }

    // 📌 Инициализация библиотеки при запуске (если её нет)
    @PostConstruct
    public void initLibrary() {
        if (!libraryRepository.existsById(1L)) {
            Library library = new Library("Main Library", List.of());
            libraryRepository.save(library);
        }
    }

    // 📌 Получаем библиотеку и список всех книг
    public LibraryDTO getLibraryWithBooks() {
        Library library = libraryRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Библиотека не найдена"));
        return new LibraryDTO(library.getBookTitles());
    }

    // 📌 Добавляем книгу в библиотеку (добавляет название в список bookTitles)
    // public void addBookToLibrary(String bookTitle) {
    //     Library library = libraryRepository.findById(1L)
    //             .orElseThrow(() -> new RuntimeException("Библиотека не найдена"));

    //     List<String> updatedTitles = library.getBookTitles();
    //     updatedTitles.add(bookTitle);
    //     library.setBookTitles(updatedTitles);

    //     libraryRepository.save(library);
    // }
}
