package com.example.library.repository; // com.example.library - package name, repository - subpackage name, this line shows the path to the current file.
//To correctly write paths, start from the root of the project and separate packages with a dot. To create directories, use the command "mkdir -p <path>"

import com.example.library.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;


// спринг автоматически создает следующие методы:
// save(T entity)	Сохраняет или обновляет объект в базе
// findById(ID id)	Ищет объект по id
// findAll()	Возвращает все записи
// deleteById(ID id)	Удаляет объект по id
// existsById(ID id)	Проверяет, существует ли объект по id
// count()	Возвращает количество записей в таблице


public interface BookRepository extends JpaRepository<Book, Long> {

}
