package com.example.library.service;

import com.example.library.dto.BookDTO;
import com.example.library.dto.PublisherDTO;
import com.example.library.entity.Publisher;
import com.example.library.repository.PublisherRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PublisherService {
    private final PublisherRepository publisherRepository;

    public PublisherService(PublisherRepository publisherRepository) {
        this.publisherRepository = publisherRepository;
    }

    // 📌 Получаем всех издателей
    public List<PublisherDTO> getAllPublishers() {
        return publisherRepository.findAll().stream()
                .map(publisher -> new PublisherDTO(publisher.getId(), publisher.getName()))
                .collect(Collectors.toList());
    }

    // 📌 Создаём издателя
    public PublisherDTO createPublisher(PublisherDTO publisherDTO) {
        Publisher publisher = new Publisher(publisherDTO.getName());
        publisher = publisherRepository.save(publisher);
        return new PublisherDTO(publisher.getId(), publisher.getName());
    }

    // 📌 Получаем все книги издателя
    public List<BookDTO> getBooksByPublisher(Long publisherId) {
        Publisher publisher = publisherRepository.findById(publisherId)
                .orElseThrow(() -> new RuntimeException("Издатель не найден"));

        return publisher.getBooks().stream()
                .map(book -> new BookDTO(
                        book.getId(),
                        book.getTitle(),
                        book.getAuthor().getName(),
                        book.getPublisher().getName(),
                        book.getCategories().stream().map(c -> c.getName()).collect(Collectors.toSet())
                ))
                .collect(Collectors.toList());
    }
}
