package com.example.library.repository;

import com.example.library.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

// спринг автоматически создает следующие методы:
// save(T entity)	Сохраняет или обновляет объект в базе
// findById(ID id)	Ищет объект по id
// findAll()	Возвращает все записи
// deleteById(ID id)	Удаляет объект по id
// existsById(ID id)	Проверяет, существует ли объект по id
// count()	Возвращает количество записей в таблице

@Repository 
public interface AuthorRepository extends JpaRepository<Author, Long> {
  Optional<Author> findByName(String name);
}
/**
 * This interface is an extension of Spring Data JPA's JpaRepository.
 * It provides basic CRUD operations for the Author entity.
 *
 * The interface extends JpaRepository and provides the following methods:
 * - findAll(): Returns a list of all authors in the database.
 * - findById(Long id): Returns an author with the specified ID.
 * - save(Author author): Saves or updates an author in the database.
 * - delete(Author author): Deletes an author from the database.
 * - deleteById(Long id): Deletes an author with the specified ID from the database.
 *
 * The interface also provides additional methods for querying the database,
 * such as findByFirstName(String firstName) and findByLastName(String lastName).
 *
 * The methods in this interface are implemented by Spring Data JPA, so we don't need to write any implementation code.
 */

