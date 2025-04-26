// src/main/java/com/lab03/sport/validation/PublisherValidation.java
package com.lab03.sport.validation;

import com.lab03.sport.repository.PublisherDAO;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class PublisherValidation {

    private final PublisherDAO publisherDAO;

    public PublisherValidation(PublisherDAO publisherDAO) {
        this.publisherDAO = publisherDAO;
    }

    /**
     * Проверяет, что издатель с таким ID существует.
     */
    public void validate(Long publisherId, Errors errors) {
        if (publisherId == null || publisherDAO.getPublisherById(publisherId).isEmpty()) {
            errors.rejectValue("publisher.pubId",
                    "NotFound",
                    "Издатель с ID=" + publisherId + " не найден");
        }
    }
}
