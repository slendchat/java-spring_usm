package com.example.library.repository;

import com.example.library.entity.Book;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class BookDao {

    private final SessionFactory sessionFactory;

    public BookDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Book save(Book book) {
        Session session = sessionFactory.getCurrentSession();
        session.persist(book);
        return book;
    }

    public Optional<Book> findById(Long id) {
        return Optional.ofNullable(sessionFactory.getCurrentSession().get(Book.class, id));
    }

    public List<Book> findAll() {
        return sessionFactory.getCurrentSession()
                .createQuery("from Book", Book.class)
                .getResultList();
    }

    public void delete(Book book) {
        sessionFactory.getCurrentSession().remove(book);
    }

    public boolean existsById(Long id) {
        return findById(id).isPresent();
    }

    public void deleteById(Long id) {
        findById(id).ifPresent(this::delete);
    }
}
