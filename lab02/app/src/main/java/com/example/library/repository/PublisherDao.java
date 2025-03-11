package com.example.library.repository;

import com.example.library.entity.Author;
import com.example.library.entity.Publisher;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class PublisherDao {

    private final SessionFactory sessionFactory;

    public PublisherDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Publisher save(Publisher publisher) {
        sessionFactory.getCurrentSession().persist(publisher);
        return publisher;
    }

    public Optional<Publisher> findByName(String name) {
        Session session = sessionFactory.getCurrentSession();
        Publisher publisher = session.createQuery("FROM Publisher WHERE name = :name", Publisher.class)
                .setParameter("name", name)
                .uniqueResult();
        return Optional.ofNullable(publisher);
    }

    public Optional<Publisher> findById(Long id) {
        return Optional.ofNullable(sessionFactory.getCurrentSession().get(Publisher.class, id));
    }

    public List<Publisher> findAll() {
        return sessionFactory.getCurrentSession()
                .createQuery("from Publisher", Publisher.class)
                .getResultList();
    }
}
