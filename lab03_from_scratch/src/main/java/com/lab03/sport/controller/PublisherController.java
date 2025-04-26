package com.lab03.sport.controller;

import com.lab03.sport.dto.PublisherDTO;
import com.lab03.sport.service.PublisherService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/publishers")
public class PublisherController {

    private final PublisherService publisherService;

    public PublisherController(PublisherService publisherService) {
        this.publisherService = publisherService;
    }

    /**
     * POST /publishers
     * Создание издателя.
     * @param pubId   ID издателя (может быть null при создании новой записи)
     * @param pubName название издателя
     */
    @PostMapping
    public ResponseEntity<PublisherDTO> createPublisher(
            @RequestParam(required = false) Long pubId,
            @RequestParam String pubName
    ) {
        PublisherDTO created = publisherService.createPublisher(pubId, pubName);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    /**
     * PUT /publishers/{id}
     * Обновление издателя по ID.
     * @param id      существующий ID издателя
     * @param pubName новое название издателя
     */
    @PutMapping("/{id}")
    public ResponseEntity<PublisherDTO> updatePublisher(
            @PathVariable("id") Long id,
            @RequestParam String pubName
    ) {
        PublisherDTO updated = publisherService.updatePublisher(id, pubName);
        return ResponseEntity.ok(updated);
    }

    /**
     * GET /publishers
     * Получение всех издателей.
     */
    @GetMapping
    public List<PublisherDTO> getAllPublishers() {
        return publisherService.getAllPublishers();
    }

    /**
     * GET /publishers/{id}
     * Получение издателя по ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PublisherDTO> getPublisherById(@PathVariable("id") Long id) {
        PublisherDTO dto = publisherService.getPublisherById(id);
        return ResponseEntity.ok(dto);
    }

    /**
     * DELETE /publishers/{id}
     * Удаление издателя по ID.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePublisherById(@PathVariable("id") Long id) {
        publisherService.deletePublisherById(id);
        return ResponseEntity.noContent().build();
    }
}
