package com.lab03.sport.repository;

import com.lab03.sport.entity.Book;
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
public class BookDAO {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final RowMapper<Book> bookRowMapper = new RowMapper<>() {
        @Override
        public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
            Book b = new Book();
            b.setBookId(rs.getLong("book_id"));
            b.setBookName(rs.getString("book_name"));
            b.setAuthorId(rs.getLong("author_id"));
            b.setPublisherId(rs.getLong("publisher_id"));
            b.setCategoryId(rs.getLong("category_id"));
            b.setLibraryId(rs.getLong("library_id"));
            return b;
        }
    };

    public BookDAO(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Создание книги. Возвращает сгенерированный book_id.
     */
    public Long createBook(Book book) {
        String sql = "INSERT INTO BOOK (book_name, author_id, publisher_id, category_id, library_id) " +
                     "VALUES (:bookName, :authorId, :publisherId, :categoryId, :libraryId)";
                     
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("bookName", book.getBookName())
                .addValue("authorId", book.getAuthorId())
                .addValue("publisherId", book.getPublisherId())
                .addValue("categoryId", book.getCategoryId())
                .addValue("libraryId", book.getLibraryId());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(sql, params, keyHolder);
        return keyHolder.getKey().longValue();
    }

    /**
     * Обновление книги по book_id. Возвращает количество обновленных строк.
     */
    public int updateBook(Book book) {
        String sql = "UPDATE BOOK SET book_name = :bookName, author_id = :authorId, " +
                     "publisher_id = :publisherId, category_id = :categoryId, library_id = :libraryId " +
                     "WHERE book_id = :bookId";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("bookName", book.getBookName())
                .addValue("authorId", book.getAuthorId())
                .addValue("publisherId", book.getPublisherId())
                .addValue("categoryId", book.getCategoryId())
                .addValue("libraryId", book.getLibraryId())
                .addValue("bookId", book.getBookId());
        return jdbcTemplate.update(sql, params);
    }

    /**
     * Получить все книги.
     */
    public List<Book> getAllBooks() {
        String sql = "SELECT * FROM BOOK";
        return jdbcTemplate.query(sql, bookRowMapper);
    }

    /**
     * Получить книгу по id.
     */
    public Optional<Book> getBookById(Long id) {
        String sql = "SELECT * FROM BOOK WHERE book_id = :bookId";
        MapSqlParameterSource params = new MapSqlParameterSource().addValue("bookId", id);
        List<Book> list = jdbcTemplate.query(sql, params, bookRowMapper);
        return list.stream().findFirst();
    }

    /**
     * Удалить книгу по id. Возвращает количество удаленных строк.
     */
    public int deleteBookById(Long id) {
        String sql = "DELETE FROM BOOK WHERE book_id = :bookId";
        MapSqlParameterSource params = new MapSqlParameterSource().addValue("bookId", id);
        return jdbcTemplate.update(sql, params);
    }
}
