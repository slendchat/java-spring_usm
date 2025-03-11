package com.example.library.repository;

import com.example.library.entity.Author;
import com.example.library.entity.Category;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CategoryDao {

    private final SessionFactory sessionFactory;

    public CategoryDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Category save(Category category) {
        sessionFactory.getCurrentSession().persist(category);
        return category;
    }

    public List<Category> findAll() {
        return sessionFactory.getCurrentSession()
                .createQuery("from Category", Category.class)
                .getResultList();
    }

    public Optional<Category> findById(Long id) {
        return Optional.ofNullable(sessionFactory.getCurrentSession().get(Category.class, id));
    }

    public Optional<Category> findByName(String name) {
        Session session = sessionFactory.getCurrentSession();
        Category category = session.createQuery("FROM Category WHERE name = :name", Category.class)
                .setParameter("name", name)
                .uniqueResult();
        return Optional.ofNullable(category);
    }
}
