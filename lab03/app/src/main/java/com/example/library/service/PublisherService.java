package com.example.library.service;

import com.example.library.dto.BookDTO;
import com.example.library.dto.PublisherDTO;
import com.example.library.entity.Publisher;
import com.example.library.repository.PublisherDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class PublisherService {

    private final PublisherDao publisherDao;
    private final BookService bookService;

    public PublisherService(PublisherDao publisherDao, BookService bookService) {
        this.publisherDao = publisherDao;
        this.bookService = bookService;
    }

    /**
     * Создать нового издателя
     */
    public PublisherDTO createPublisher(PublisherDTO dto) {
        // Проверка связанных книг
        Set<Long> bookIds = dto.getBookIds() != null ? dto.getBookIds() : Collections.emptySet();
        for (Long bookId : bookIds) {
            bookService.getBookById(bookId);
        }
        Publisher entity = new Publisher();
        entity.setName(dto.getName());
        entity.setBookIds(List.copyOf(bookIds));
        Publisher saved = publisherDao.save(entity);
        return toDto(saved, Collections.emptySet());
    }

    /**
     * Получить всех издателей (без книг)
     */
    public List<PublisherDTO> getAllPublishers() {
        return publisherDao.findAll().stream()
                .map(p -> toDto(p, Collections.emptySet()))
                .collect(Collectors.toList());
    }

    /**
     * Получить издателя по ID с деталями книг
     */
    public PublisherDTO getPublisherById(Long id) {
        Publisher p = publisherDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Publisher not found: " + id));
        Set<BookDTO> books = p.getBookIds().stream()
                .map(bookService::getBookById)
                .collect(Collectors.toSet());
        return toDto(p, books);
    }

    /**
     * Обновить существующего издателя
     */
    public PublisherDTO updatePublisher(Long id, PublisherDTO dto) {
        Publisher entity = publisherDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Publisher not found: " + id));
        entity.setName(dto.getName());
        Set<Long> bookIds = dto.getBookIds() != null ? dto.getBookIds() : Collections.emptySet();
        for (Long bookId : bookIds) {
            bookService.getBookById(bookId);
        }
        entity.setBookIds(List.copyOf(bookIds));
        Publisher updated = publisherDao.save(entity);
        return toDto(updated, Collections.emptySet());
    }

    /**
     * Удалить издателя по ID
     */
    public void deletePublisher(Long id) {
        publisherDao.deleteById(id);
    }

    /**
     * Вспомогательный метод для создания DTO
     */
    private PublisherDTO toDto(Publisher p, Set<BookDTO> books) {
        return new PublisherDTO(
                p.getId(),
                p.getName(),
                new HashSet<>(p.getBookIds()),
                books != null ? books : Collections.emptySet()
        );
    }
}
