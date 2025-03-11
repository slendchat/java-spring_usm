package com.example.library.repository;

import com.example.library.entity.Library;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class LibraryDao {

    private final SessionFactory sessionFactory;

    public LibraryDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Library save(Library library) {
        sessionFactory.getCurrentSession().persist(library);
        return library;
    }

    public Optional<Library> findById(Long id) {
        Session session = sessionFactory.getCurrentSession();
        Library library = session.get(Library.class, id);
        return Optional.ofNullable(library);
    }
}
