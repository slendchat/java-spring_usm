-- Удаляем старые таблицы, чтобы можно было перезапускать в памяти
DROP TABLE IF EXISTS BOOK;
DROP TABLE IF EXISTS AUTHOR;
DROP TABLE IF EXISTS PUBLISHER;
DROP TABLE IF EXISTS CATEGORY;
DROP TABLE IF EXISTS LIBRARY;

-- Родительские сущности
CREATE TABLE LIBRARY (
  lib_id    INT AUTO_INCREMENT PRIMARY KEY
);

CREATE TABLE AUTHOR (
  auth_id   INT AUTO_INCREMENT PRIMARY KEY,
  auth_name VARCHAR(255) NOT NULL
);

CREATE TABLE PUBLISHER (
  pub_id    INT AUTO_INCREMENT PRIMARY KEY,
  pub_name  VARCHAR(255) NOT NULL
);

CREATE TABLE CATEGORY (
  cat_id    INT AUTO_INCREMENT PRIMARY KEY,
  cat_name  VARCHAR(255) NOT NULL
);

-- Таблица книг с внешними ключами
CREATE TABLE BOOK (
  book_id      INT AUTO_INCREMENT PRIMARY KEY,
  book_name    VARCHAR(255) NOT NULL,
  author_id    INT NOT NULL,
  publisher_id INT NOT NULL,
  category_id  INT NOT NULL,
  library_id   INT NOT NULL,
  FOREIGN KEY (author_id)    REFERENCES AUTHOR   (auth_id),
  FOREIGN KEY (publisher_id) REFERENCES PUBLISHER(pub_id),
  FOREIGN KEY (category_id)  REFERENCES CATEGORY (cat_id),
  FOREIGN KEY (library_id)   REFERENCES LIBRARY  (lib_id)
);
