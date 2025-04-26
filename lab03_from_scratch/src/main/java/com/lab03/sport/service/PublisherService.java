package com.lab03.sport.service;

import com.lab03.sport.dto.PublisherDTO;
import com.lab03.sport.entity.Publisher;
import com.lab03.sport.repository.PublisherDAO;
import com.lab03.sport.validation.PublisherValidation;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PublisherService {

    private final PublisherDAO publisherDAO;
    private final PublisherValidation publisherValidation;

    public PublisherService(PublisherDAO publisherDAO,
                            PublisherValidation publisherValidation) {
        this.publisherDAO = publisherDAO;
        this.publisherValidation = publisherValidation;
    }

    /**
     * Создание издателя.
     * @param pubId   можно передать null при создании
     * @param pubName имя издателя
     * @return DTO созданного издателя с присвоенным ID
     */
    public PublisherDTO createPublisher(Long pubId, String pubName) {
        PublisherDTO dto = new PublisherDTO(pubId, pubName);
        Errors errors = new BeanPropertyBindingResult(dto, "publisherDTO");
        // проверяем существующий ID, если передан
        if (pubId != null) {
            publisherValidation.validate(pubId, errors);
        }
        if (errors.hasErrors()) {
            throw new IllegalArgumentException(errors.toString());
        }

        Publisher entity = new Publisher();
        entity.setPubName(pubName);
        Long newId = publisherDAO.createPublisher(entity);
        return new PublisherDTO(newId, pubName);
    }

    /**
     * Обновление издателя по ID.
     * @param pubId   существующий ID издателя
     * @param pubName новое имя
     * @return DTO обновлённого издателя
     */
    public PublisherDTO updatePublisher(Long pubId, String pubName) {
        PublisherDTO dto = new PublisherDTO(pubId, pubName);
        Errors errors = new BeanPropertyBindingResult(dto, "publisherDTO");
        publisherValidation.validate(pubId, errors);
        if (errors.hasErrors()) {
            throw new IllegalArgumentException(errors.toString());
        }

        Publisher entity = new Publisher();
        entity.setPubId(pubId);
        entity.setPubName(pubName);
        publisherDAO.updatePublisher(entity);
        return getPublisherById(pubId);
    }

    /**
     * Получение всех издателей.
     */
    public List<PublisherDTO> getAllPublishers() {
        return publisherDAO.getAllPublishers().stream()
                .map(p -> new PublisherDTO(p.getPubId(), p.getPubName()))
                .collect(Collectors.toList());
    }

    /**
     * Получение издателя по ID с проверкой существования.
     */
    public PublisherDTO getPublisherById(Long pubId) {
        Errors errors = new BeanPropertyBindingResult(new PublisherDTO(pubId, null), "publisherDTO");
        publisherValidation.validate(pubId, errors);
        if (errors.hasErrors()) {
            throw new IllegalArgumentException(errors.toString());
        }

        Publisher p = publisherDAO.getPublisherById(pubId)
                .orElseThrow(() -> new IllegalArgumentException("Издатель с ID=" + pubId + " не найден"));
        return new PublisherDTO(p.getPubId(), p.getPubName());
    }

    /**
     * Удаление издателя по ID с проверкой существования.
     */
    public void deletePublisherById(Long pubId) {
        Errors errors = new BeanPropertyBindingResult(new PublisherDTO(pubId, null), "publisherDTO");
        publisherValidation.validate(pubId, errors);
        if (errors.hasErrors()) {
            throw new IllegalArgumentException(errors.toString());
        }
        publisherDAO.deletePublisherById(pubId);
    }
}
