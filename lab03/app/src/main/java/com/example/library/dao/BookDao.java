package com.example.library.repository;

import com.example.library.entity.Book;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class BookDao {

    private final NamedParameterJdbcTemplate npJdbc;

    public BookDao(NamedParameterJdbcTemplate npJdbc) {
        this.npJdbc = npJdbc;
    }

    // RowMapper для полей Book
    private final RowMapper<Book> bookRowMapper = new RowMapper<>() {
        @Override
        public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
            Book book = new Book();
            book.setId(rs.getLong("id"));
            book.setTitle(rs.getString("title"));
            book.setAuthorId(rs.getLong("author_id"));
            book.setPublisherId(rs.getLong("publisher_id"));
            return book;
        }
    };

    // Получить список categoryIds для книги
    private List<Long> findCategoryIdsByBookId(Long bookId) {
        String sql = "SELECT category_id FROM book_category WHERE book_id = :bookId";
        MapSqlParameterSource params = new MapSqlParameterSource("bookId", bookId);
        return npJdbc.queryForList(sql, params, Long.class);
    }

    public Optional<Book> findById(Long id) {
        String sql = "SELECT id, title, author_id, publisher_id FROM book WHERE id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource("id", id);
        List<Book> books = npJdbc.query(sql, params, bookRowMapper);
        if (books.isEmpty()) {
            return Optional.empty();
        }
        Book book = books.get(0);
        book.setCategoryIds(findCategoryIdsByBookId(id));
        return Optional.of(book);
    }

    public List<Book> findAll() {
        String sql = "SELECT id, title, author_id, publisher_id FROM book";
        List<Book> books = npJdbc.query(sql, new MapSqlParameterSource(), bookRowMapper);
        for (Book book : books) {
            book.setCategoryIds(findCategoryIdsByBookId(book.getId()));
        }
        return books;
    }

    public Book save(Book book) {
        if (book.getId() == null) {
            // INSERT
            String sql = "INSERT INTO book (title, author_id, publisher_id) VALUES (:title, :authorId, :publisherId)";
            MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("title", book.getTitle())
                .addValue("authorId", book.getAuthorId())
                .addValue("publisherId", book.getPublisherId());
            KeyHolder keyHolder = new GeneratedKeyHolder();
            npJdbc.update(sql, params, keyHolder, new String[]{"id"});
            book.setId(keyHolder.getKey().longValue());
        } else {
            // UPDATE
            String sql = "UPDATE book SET title = :title, author_id = :authorId, publisher_id = :publisherId WHERE id = :id";
            MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("title", book.getTitle())
                .addValue("authorId", book.getAuthorId())
                .addValue("publisherId", book.getPublisherId())
                .addValue("id", book.getId());
            npJdbc.update(sql, params);

            // Очистить старые связи
            String deleteSql = "DELETE FROM book_category WHERE book_id = :id";
            npJdbc.update(deleteSql, params);
        }

        // Сохранить связи с категориями
        if (book.getCategoryIds() != null && !book.getCategoryIds().isEmpty()) {
            String insertCatSql = "INSERT INTO book_category (book_id, category_id) VALUES (:bookId, :categoryId)";
            for (Long catId : book.getCategoryIds()) {
                MapSqlParameterSource params = new MapSqlParameterSource()
                    .addValue("bookId", book.getId())
                    .addValue("categoryId", catId);
                npJdbc.update(insertCatSql, params);
            }
        }
        return book;
    }

    public int deleteById(Long id) {
        // Удалить связи
        String deleteCatSql = "DELETE FROM book_category WHERE book_id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource("id", id);
        npJdbc.update(deleteCatSql, params);
        // Удалить книгу
        String deleteSql = "DELETE FROM book WHERE id = :id";
        return npJdbc.update(deleteSql, params);
    }

    public boolean existsById(Long id) {
        String sql = "SELECT COUNT(*) FROM book WHERE id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource("id", id);
        Integer count = npJdbc.queryForObject(sql, params, Integer.class);
        return count != null && count > 0;
    }
}
