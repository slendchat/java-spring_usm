package com.example.library.repository;

import com.example.library.entity.Author;
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
public class AuthorDao {

    private final NamedParameterJdbcTemplate npJdbc;

    public AuthorDao(NamedParameterJdbcTemplate npJdbc) {
        this.npJdbc = npJdbc;
    }

    // RowMapper для Author
    private final RowMapper<Author> authorRowMapper = new RowMapper<>() {
        @Override
        public Author mapRow(ResultSet rs, int rowNum) throws SQLException {
            Author author = new Author();
            author.setId(rs.getLong("id"));
            author.setName(rs.getString("name"));
            return author;
        }
    };

    // Получить список bookIds у автора
    private List<Long> findBookIdsByAuthorId(Long authorId) {
        String sql = "SELECT id FROM book WHERE author_id = :authorId";
        MapSqlParameterSource params = new MapSqlParameterSource("authorId", authorId);
        return npJdbc.queryForList(sql, params, Long.class);
    }

    public Optional<Author> findById(Long id) {
        String sql = "SELECT id, name FROM author WHERE id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource("id", id);
        List<Author> authors = npJdbc.query(sql, params, authorRowMapper);
        if (authors.isEmpty()) {
            return Optional.empty();
        }
        Author author = authors.get(0);
        author.setBookIds(findBookIdsByAuthorId(id));
        return Optional.of(author);
    }

    public List<Author> findAll() {
        String sql = "SELECT id, name FROM author";
        List<Author> authors = npJdbc.query(sql, new MapSqlParameterSource(), authorRowMapper);
        for (Author author : authors) {
            author.setBookIds(findBookIdsByAuthorId(author.getId()));
        }
        return authors;
    }

    public Author save(Author author) {
        if (author.getId() == null) {
            // INSERT
            String sql = "INSERT INTO author (name) VALUES (:name)";
            MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", author.getName());
            KeyHolder keyHolder = new GeneratedKeyHolder();
            npJdbc.update(sql, params, keyHolder, new String[]{"id"});
            author.setId(keyHolder.getKey().longValue());
        } else {
            // UPDATE
            String sql = "UPDATE author SET name = :name WHERE id = :id";
            MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", author.getName())
                .addValue("id", author.getId());
            npJdbc.update(sql, params);
        }
        return author;
    }

    public int deleteById(Long id) {
        String sql = "DELETE FROM author WHERE id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource("id", id);
        return npJdbc.update(sql, params);
    }

    public Optional<Author> findByName(String name) {
        String sql = "SELECT id, name FROM author WHERE name = :name";
        MapSqlParameterSource params = new MapSqlParameterSource("name", name);
        List<Author> authors = npJdbc.query(sql, params, authorRowMapper);
        if (authors.isEmpty()) {
            return Optional.empty();
        }
        Author author = authors.get(0);
        author.setBookIds(findBookIdsByAuthorId(author.getId()));
        return Optional.of(author);
    }
}
