CREATE TABLE author (
  id   BIGSERIAL PRIMARY KEY,
  name VARCHAR(255) NOT NULL
);

CREATE TABLE publisher (
  id   BIGSERIAL PRIMARY KEY,
  name VARCHAR(255) NOT NULL
);

CREATE TABLE category (
  id   BIGSERIAL PRIMARY KEY,
  name VARCHAR(255) NOT NULL
);

CREATE TABLE book (
  id           BIGSERIAL PRIMARY KEY,
  title        VARCHAR(255) NOT NULL,
  author_id    BIGINT NOT NULL REFERENCES author(id),
  publisher_id BIGINT NOT NULL REFERENCES publisher(id),
  library_id   BIGINT NULL   REFERENCES library(id)
);

CREATE TABLE book_category (
  book_id     BIGINT REFERENCES book(id) ON DELETE CASCADE,
  category_id BIGINT REFERENCES category(id) ON DELETE CASCADE,
  PRIMARY KEY (book_id, category_id)
);

CREATE TABLE library (
  id   BIGSERIAL PRIMARY KEY,
  name VARCHAR(255) NOT NULL
);
