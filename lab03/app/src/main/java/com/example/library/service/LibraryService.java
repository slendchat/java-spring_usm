package com.example.library.service;

import com.example.library.dto.LibraryDTO;
import com.example.library.entity.Library;
import com.example.library.repository.LibraryDao;
import org.springframework.context.event.EventListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class LibraryService {

    private final LibraryDao libraryDao;

    public LibraryService(LibraryDao libraryDao) {
        this.libraryDao = libraryDao;
    }

    /**
     * Инициализирует библиотеку при старте приложения, если её ещё нет в БД.
     */
    @EventListener(ContextRefreshedEvent.class)
    public void initLibrary() {
        if (libraryDao.findById(1L).isEmpty()) {
            Library library = new Library();
            library.setName("Main Library");
            library.setBookTitles(new ArrayList<>());
            libraryDao.save(library);
        }
    }

    /**
     * Получить информацию о библиотеке вместе со списком книг
     */
    public LibraryDTO getLibrary() {
        Library library = libraryDao.findById(1L)
                .orElseThrow(() -> new RuntimeException("Library not found"));
        return toDto(library);
    }

    /**
     * Обновить информацию о библиотеке (имя и список заголовков книг)
     */
    public LibraryDTO updateLibrary(Long id, LibraryDTO dto) {
        Library library = libraryDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Library not found: " + id));
        library.setName(dto.getName());
        List<String> titles = dto.getBookTitles() != null ? dto.getBookTitles() : new ArrayList<>();
        library.setBookTitles(new ArrayList<>(titles));
        Library saved = libraryDao.save(library);
        return toDto(saved);
    }

    /**
     * Преобразование entity в DTO
     */
    private LibraryDTO toDto(Library library) {
        return new LibraryDTO(
            library.getId(),
            library.getName(),
            library.getBookTitles()
        );
    }

    /**
     * Получить все библиотеки
     */
    public List<LibraryDTO> getAllLibraries() {
        return libraryDao.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Получить библиотеку по ID
     */
    public LibraryDTO getLibraryById(Long id) {
        Library library = libraryDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Library not found: " + id));
        return toDto(library);
    }

    /**
     * Создать новую библиотеку
     */
    public LibraryDTO createLibrary(LibraryDTO dto) {
        Library library = new Library();
        library.setName(dto.getName());
        library.setBookTitles(dto.getBookTitles() != null
                ? new ArrayList<>(dto.getBookTitles())
                : new ArrayList<>());
        Library saved = libraryDao.save(library);
        return toDto(saved);
    }
    
    /**
     * Удалить библиотеку по ID
     */
    public void deleteLibrary(Long id) {
        libraryDao.deleteById(id);
    }
}
