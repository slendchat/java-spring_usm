package com.example.library.controller;

import com.example.library.dto.BookDTO;
import com.example.library.dto.PublisherDTO;
import com.example.library.service.PublisherService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/publishers")
@Validated
public class PublisherController {

    private final PublisherService publisherService;

    public PublisherController(PublisherService publisherService) {
        this.publisherService = publisherService;
    }

    /**
     * Получить всех издателей (без книг)
     */
    @GetMapping
    public ResponseEntity<List<PublisherDTO>> getAllPublishers() {
        List<PublisherDTO> list = publisherService.getAllPublishers();
        return ResponseEntity.ok(list);
    }

    /**
     * Получить издателя по ID (с деталями книг)
     */
    @GetMapping("/{id}")
    public ResponseEntity<PublisherDTO> getPublisherById(@PathVariable Long id) {
        PublisherDTO dto = publisherService.getPublisherById(id);
        return ResponseEntity.ok(dto);
    }

    /**
     * Создать нового издателя
     */
    @PostMapping
    public ResponseEntity<PublisherDTO> createPublisher(@Valid @RequestBody PublisherDTO publisherDTO) {
        PublisherDTO created = publisherService.createPublisher(publisherDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Обновить существующего издателя
     */
    @PutMapping("/{id}")
    public ResponseEntity<PublisherDTO> updatePublisher(@PathVariable Long id,
                                                       @Valid @RequestBody PublisherDTO publisherDTO) {
        PublisherDTO updated = publisherService.updatePublisher(id, publisherDTO);
        return ResponseEntity.ok(updated);
    }

    /**
     * Удалить издателя по ID
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePublisher(@PathVariable Long id) {
        publisherService.deletePublisher(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Получить список книг издателя по ID
     */
    @GetMapping("/{id}/books")
    public ResponseEntity<Set<BookDTO>> getBooksByPublisher(@PathVariable Long id) {
        PublisherDTO dto = publisherService.getPublisherById(id);
        return ResponseEntity.ok(dto.getBooks());
    }
}
