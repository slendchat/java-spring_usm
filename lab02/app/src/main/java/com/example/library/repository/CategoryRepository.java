package com.example.library.repository;

import com.example.library.entity.Category;
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
public interface CategoryRepository  extends JpaRepository<Category, Long> {
  Optional<Category> findByName(String name);
}