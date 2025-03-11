package com.example.library.repository;

import com.example.library.entity.Author;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class AuthorDao {

    private final SessionFactory sessionFactory;

    public AuthorDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Optional<Author> findById(Long id) {
        return Optional.ofNullable(sessionFactory.getCurrentSession().get(Author.class, id));
    }

    public List<Author> findAll() {
        return sessionFactory.getCurrentSession()
                .createQuery("from Author", Author.class)
                .getResultList();
    }

    public Optional<Author> findByName(String name) {
        return sessionFactory.getCurrentSession()
                .createQuery("FROM Author WHERE name = :name", Author.class)
                .setParameter("name", name)
                .uniqueResultOptional();
    }

    public Author save(Author author) {
        sessionFactory.getCurrentSession().persist(author);
        return author;
    }
}