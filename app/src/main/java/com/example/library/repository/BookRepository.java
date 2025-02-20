package com.example.library.repository; // com.example.library - package name, repository - subpackage name, this line shows the path to the current file.
//To correctly write paths, start from the root of the project and separate packages with a dot. To create directories, use the command "mkdir -p <path>"

import com.example.library.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {}
