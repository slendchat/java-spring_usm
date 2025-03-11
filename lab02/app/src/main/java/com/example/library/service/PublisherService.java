package com.example.library.service;

import com.example.library.dto.BookDTO;
import com.example.library.dto.PublisherDTO;
import com.example.library.entity.Publisher;
import com.example.library.repository.PublisherDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PublisherService {

    private final PublisherDao publisherDao;

    public PublisherService(PublisherDao publisherDao) {
        this.publisherDao = publisherDao;
    }

    public List<PublisherDTO> getAllPublishers() {
        return publisherDao.findAll().stream()
                .map(publisher -> new PublisherDTO(publisher.getId(), publisher.getName()))
                .collect(Collectors.toList());
    }

    public PublisherDTO createPublisher(PublisherDTO publisherDTO) {
        Publisher publisher = new Publisher(publisherDTO.getName());
        publisherDao.save(publisher);
        return new PublisherDTO(publisher.getId(), publisher.getName());
    }

    public List<BookDTO> getBooksByPublisher(Long publisherId) {
        Publisher publisher = publisherDao.findById(publisherId)
                .orElseThrow(() -> new RuntimeException("Издатель не найден"));

        return publisher.getBooks().stream()
                .map(book -> new BookDTO(
                        book.getId(),
                        book.getTitle(),
                        book.getAuthor().getName(),
                        publisher.getName(),
                        book.getCategories().stream()
                                .map(category -> category.getName())
                                .collect(Collectors.toSet())
                ))
                .collect(Collectors.toList());
    }
}
