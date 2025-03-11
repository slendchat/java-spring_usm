package com.example.library.service;

import com.example.library.dto.LibraryDTO;
import com.example.library.entity.Library;
import com.example.library.repository.LibraryDao;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class LibraryService {

    private final LibraryDao libraryDao;

    public LibraryService(LibraryDao libraryDao) {
        this.libraryDao = libraryDao;
    }

    // Сработает только после полной инициализации контекста (включая транзакции)
    @org.springframework.context.event.EventListener(org.springframework.context.event.ContextRefreshedEvent.class)
    public void initLibrary() {
        if (libraryDao.findById(1L).isEmpty()) {
            Library library = new Library("Main Library", List.of());
            libraryDao.save(library);
        }
    }

    public LibraryDTO getLibraryWithBooks() {
        Library library = libraryDao.findById(1L)
                .orElseThrow(() -> new RuntimeException("Библиотека не найдена"));
        return new LibraryDTO(library.getBookTitles());
    }
}
